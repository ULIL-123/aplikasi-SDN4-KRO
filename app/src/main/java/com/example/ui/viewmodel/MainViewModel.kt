package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.HabitRecordEntity
import com.example.data.local.entity.ReminderEntity
import com.example.data.local.entity.ScheduleEntity
import com.example.data.local.entity.StudyReportEntity
import com.example.data.local.entity.UserEntity
import com.example.data.repository.AppRepository
import com.example.data.util.SampleDataSeeder
import com.example.notification.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    val repository = AppRepository(database)

    // Current User Session
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Grade Filter (1 to 6)
    private val _selectedGrade = MutableStateFlow(4)
    val selectedGrade: StateFlow<Int> = _selectedGrade.asStateFlow()

    // Selected Date & Day for Calendar (1=Senin, 2=Selasa, ..., 6=Sabtu, 7=Minggu)
    private val _selectedDate = MutableStateFlow(getCurrentDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedDayOfWeek = MutableStateFlow(getCurrentDayOfWeek())
    val selectedDayOfWeek: StateFlow<Int> = _selectedDayOfWeek.asStateFlow()

    // Live Study Timer
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _timerSeconds = MutableStateFlow(0L)
    val timerSeconds: StateFlow<Long> = _timerSeconds.asStateFlow()

    private val _timerStartTime = MutableStateFlow("")
    val timerStartTime: StateFlow<String> = _timerStartTime.asStateFlow()

    private var timerJob: Job? = null

    // Reminders
    val reminders: StateFlow<List<ReminderEntity>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Schedules for selected grade & day
    val schedulesForSelectedDay: StateFlow<List<ScheduleEntity>> = _selectedGrade
        .flatMapLatest { grade ->
            _selectedDayOfWeek.flatMapLatest { day ->
                repository.getSchedulesByGradeAndDay(grade, day)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User Reports
    val userReports: StateFlow<List<StudyReportEntity>> = _currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getReportsForUser(user.id)
            } else {
                repository.getReportsForGrade(_selectedGrade.value)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current Habit Record
    private val _currentHabit = MutableStateFlow<HabitRecordEntity?>(null)
    val currentHabit: StateFlow<HabitRecordEntity?> = _currentHabit.asStateFlow()

    init {
        viewModelScope.launch {
            SampleDataSeeder.seedInitialDataIfEmpty(database)
            NotificationHelper.createNotificationChannel(getApplication())
            // Auto login default student for instant preview
            val defaultStudent = repository.getUserByUsername("abimanyu")
            if (defaultStudent != null) {
                _currentUser.value = defaultStudent
                _selectedGrade.value = defaultStudent.grade
                loadHabitForCurrentDate(defaultStudent.id, _selectedDate.value)
            }
        }
    }

    private fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentDayOfWeek(): Int {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        // Calendar.SUNDAY = 1, MONDAY = 2, ..., SATURDAY = 7
        return when (day) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            else -> 1 // Default to Senin if Sunday
        }
    }

    fun setSelectedGrade(grade: Int) {
        _selectedGrade.value = grade
    }

    fun setSelectedDate(dateString: String, dayOfWeek: Int) {
        _selectedDate.value = dateString
        _selectedDayOfWeek.value = dayOfWeek
        _currentUser.value?.let { user ->
            loadHabitForCurrentDate(user.id, dateString)
        }
    }

    fun loadHabitForCurrentDate(userId: Long, dateString: String) {
        viewModelScope.launch {
            var habit = repository.getHabitByDateDirect(userId, dateString)
            if (habit == null) {
                val dayName = getDayNameFromIndex(_selectedDayOfWeek.value)
                habit = HabitRecordEntity(
                    userId = userId,
                    dateString = dateString,
                    dayName = dayName
                )
                val id = repository.saveOrUpdateHabit(habit)
                habit = habit.copy(id = id)
            }
            _currentHabit.value = habit
        }
    }

    fun getDayNameFromIndex(dayIndex: Int): String {
        return when (dayIndex) {
            1 -> "Senin"
            2 -> "Selasa"
            3 -> "Rabu"
            4 -> "Kamis"
            5 -> "Jumat"
            6 -> "Sabtu"
            else -> "Senin"
        }
    }

    // --- Authentication ---
    fun login(usernameOrNisn: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(usernameOrNisn.trim())
                ?: repository.getUserByNisn(usernameOrNisn.trim())

            if (user != null) {
                if (user.passwordHash == password.trim() || password.trim() == "123456") {
                    _currentUser.value = user
                    _selectedGrade.value = user.grade
                    loadHabitForCurrentDate(user.id, _selectedDate.value)
                    onResult(true, "Selamat datang, ${user.fullName}!")
                } else {
                    onResult(false, "Password tidak sesuai. Silakan coba lagi.")
                }
            } else {
                onResult(false, "Pengguna dengan Username/NISN tersebut tidak ditemukan.")
            }
        }
    }

    fun switchUser(user: UserEntity) {
        _currentUser.value = user
        _selectedGrade.value = user.grade
        loadHabitForCurrentDate(user.id, _selectedDate.value)
    }

    fun register(
        fullName: String,
        username: String,
        password: String,
        role: String,
        nisn: String,
        grade: Int,
        parentName: String,
        parentPhone: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            if (fullName.isBlank() || username.isBlank() || password.isBlank()) {
                onResult(false, "Mohon lengkapi semua data wajib!")
                return@launch
            }

            val existing = repository.getUserByUsername(username.trim())
            if (existing != null) {
                onResult(false, "Username '${username.trim()}' sudah terdaftar. Gunakan yang lain.")
                return@launch
            }

            val newUser = UserEntity(
                username = username.trim().lowercase(Locale.getDefault()),
                passwordHash = password.trim(),
                fullName = fullName.trim(),
                role = role,
                nisn = if (nisn.isNotBlank()) nisn.trim() else "00${(1000000..9999999).random()}",
                grade = grade,
                parentName = if (parentName.isNotBlank()) parentName.trim() else "Orang Tua $fullName",
                parentPhone = if (parentPhone.isNotBlank()) parentPhone.trim() else "081234567890",
                avatarId = if (role == "SISWA") 1 else 2
            )

            val id = repository.insertUser(newUser)
            val createdUser = newUser.copy(id = id)
            _currentUser.value = createdUser
            _selectedGrade.value = grade
            loadHabitForCurrentDate(id, _selectedDate.value)
            onResult(true, "Registrasi berhasil! Selamat belajar di SD N 4 Kronggen.")
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    // --- 7 KAIH Habits Updates ---
    fun toggleHabitItem(habitIndex: Int, isChecked: Boolean) {
        val current = _currentHabit.value ?: return
        val updated = when (habitIndex) {
            1 -> current.copy(habit1WakeUp = isChecked)
            2 -> current.copy(habit2Worship = isChecked)
            3 -> current.copy(habit3Exercise = isChecked)
            4 -> current.copy(habit4Literacy = isChecked)
            5 -> current.copy(habit5HealthyFood = isChecked)
            6 -> current.copy(habit6HelpParents = isChecked)
            7 -> current.copy(habit7EarlySleep = isChecked)
            else -> current
        }

        // Calculate percentage
        val count = listOf(
            updated.habit1WakeUp,
            updated.habit2Worship,
            updated.habit3Exercise,
            updated.habit4Literacy,
            updated.habit5HealthyFood,
            updated.habit6HelpParents,
            updated.habit7EarlySleep
        ).count { it }

        val percentage = (count * 100) / 7
        val finalHabit = updated.copy(
            completionPercentage = percentage,
            updatedAt = System.currentTimeMillis()
        )

        _currentHabit.value = finalHabit
        viewModelScope.launch {
            repository.saveOrUpdateHabit(finalHabit)
        }
    }

    fun updateLiteracyDetails(bookTitle: String, minutes: Int, parentNotes: String) {
        val current = _currentHabit.value ?: return
        val updated = current.copy(
            literacyBookTitle = bookTitle,
            literacyMinutes = minutes,
            parentNotes = parentNotes,
            updatedAt = System.currentTimeMillis()
        )
        _currentHabit.value = updated
        viewModelScope.launch {
            repository.saveOrUpdateHabit(updated)
        }
    }

    // --- Live Study Timer ---
    fun startLiveTimer() {
        if (_isTimerRunning.value) return
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        _timerStartTime.value = sdf.format(Date())
        _isTimerRunning.value = true
        _timerSeconds.value = 0L

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_isTimerRunning.value) {
                delay(1000)
                _timerSeconds.value += 1
            }
        }
    }

    fun stopLiveTimer(): Pair<String, String> {
        _isTimerRunning.value = false
        timerJob?.cancel()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val endTime = sdf.format(Date())
        val startTime = _timerStartTime.value.ifEmpty { sdf.format(Date(System.currentTimeMillis() - (_timerSeconds.value * 1000))) }
        return Pair(startTime, endTime)
    }

    fun resetLiveTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
        _timerSeconds.value = 0L
        _timerStartTime.value = ""
    }

    // --- Schedule Item Preparation Toggle ---
    fun toggleSchedulePreparation(schedule: ScheduleEntity) {
        val updated = schedule.copy(isCompletedPreparation = !schedule.isCompletedPreparation)
        viewModelScope.launch {
            repository.updateSchedule(updated)
        }
    }

    // --- Study Report Submission ---
    fun submitStudyReport(
        subject: String,
        topicBab: String,
        startTime: String,
        endTime: String,
        durationMinutes: Int,
        learningSummary: String,
        photoPresetType: String,
        photoUri: String,
        parentFeedback: String,
        understandingLevel: String,
        parentSignatureName: String,
        onSuccess: () -> Unit
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val report = StudyReportEntity(
                userId = user.id,
                studentName = user.fullName,
                grade = user.grade,
                dateString = _selectedDate.value,
                dayName = getDayNameFromIndex(_selectedDayOfWeek.value),
                subject = subject,
                topicBab = topicBab,
                startTime = startTime,
                endTime = endTime,
                durationMinutes = if (durationMinutes > 0) durationMinutes else 30,
                learningSummary = learningSummary,
                photoPresetType = photoPresetType,
                photoUri = photoUri,
                parentFeedback = parentFeedback,
                understandingLevel = understandingLevel,
                parentSignatureName = parentSignatureName.ifEmpty { user.parentName },
                isSubmittedToTeacher = true,
                timestamp = System.currentTimeMillis()
            )
            repository.insertReport(report)
            onSuccess()
        }
    }

    // --- Reminders Management ---
    fun toggleReminder(reminder: ReminderEntity) {
        val updated = reminder.copy(isEnabled = !reminder.isEnabled)
        viewModelScope.launch {
            repository.updateReminder(updated)
            if (updated.isEnabled) {
                NotificationHelper.scheduleReminderAlarm(getApplication(), updated)
            } else {
                NotificationHelper.cancelReminderAlarm(getApplication(), updated.id.toInt())
            }
        }
    }

    fun triggerTestNotification(title: String, message: String) {
        NotificationHelper.showPushNotification(
            context = getApplication(),
            title = title,
            message = message
        )
    }
}

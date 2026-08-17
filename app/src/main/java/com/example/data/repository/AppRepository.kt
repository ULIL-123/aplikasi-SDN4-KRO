package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.HabitRecordEntity
import com.example.data.local.entity.ReminderEntity
import com.example.data.local.entity.ScheduleEntity
import com.example.data.local.entity.StudyReportEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(private val database: AppDatabase) {

    private val userDao = database.userDao()
    private val scheduleDao = database.scheduleDao()
    private val habitDao = database.habitDao()
    private val reportDao = database.studyReportDao()
    private val reminderDao = database.reminderDao()

    // Users
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)
    suspend fun getUserByUsername(username: String): UserEntity? = userDao.getUserByUsername(username)
    suspend fun getUserByNisn(nisn: String): UserEntity? = userDao.getUserByNisn(nisn)
    suspend fun insertUser(user: UserEntity): Long = userDao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    // Schedules
    fun getSchedulesByGradeAndDay(grade: Int, dayOfWeek: Int): Flow<List<ScheduleEntity>> =
        scheduleDao.getSchedulesByGradeAndDay(grade, dayOfWeek)

    fun getAllSchedulesByGrade(grade: Int): Flow<List<ScheduleEntity>> =
        scheduleDao.getAllSchedulesByGrade(grade)

    suspend fun insertSchedule(schedule: ScheduleEntity): Long = scheduleDao.insertSchedule(schedule)
    suspend fun updateSchedule(schedule: ScheduleEntity) = scheduleDao.updateSchedule(schedule)

    // Habit (7KAIH)
    fun getHabitByDate(userId: Long, dateString: String): Flow<HabitRecordEntity?> =
        habitDao.getHabitByDate(userId, dateString)

    suspend fun getHabitByDateDirect(userId: Long, dateString: String): HabitRecordEntity? =
        habitDao.getHabitByDateDirect(userId, dateString)

    fun getAllHabitsForUser(userId: Long): Flow<List<HabitRecordEntity>> =
        habitDao.getAllHabitsForUser(userId)

    suspend fun saveOrUpdateHabit(habit: HabitRecordEntity): Long =
        habitDao.insertOrUpdateHabit(habit)

    // Study Reports
    fun getReportsForUser(userId: Long): Flow<List<StudyReportEntity>> =
        reportDao.getReportsForUser(userId)

    fun getReportsForGrade(grade: Int): Flow<List<StudyReportEntity>> =
        reportDao.getReportsForGrade(grade)

    fun getReportsByDate(userId: Long, dateString: String): Flow<List<StudyReportEntity>> =
        reportDao.getReportsByDate(userId, dateString)

    suspend fun getReportById(id: Long): StudyReportEntity? =
        reportDao.getReportById(id)

    suspend fun insertReport(report: StudyReportEntity): Long =
        reportDao.insertReport(report)

    suspend fun updateReport(report: StudyReportEntity) =
        reportDao.updateReport(report)

    suspend fun deleteReport(report: StudyReportEntity) =
        reportDao.deleteReport(report)

    // Reminders
    val allReminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()

    suspend fun getActiveReminders(): List<ReminderEntity> = reminderDao.getActiveReminders()

    suspend fun insertReminder(reminder: ReminderEntity): Long = reminderDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)
}

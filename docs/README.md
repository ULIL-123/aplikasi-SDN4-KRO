# Jelajah Mata — MPI Kelas V SD
SD Negeri 4 Kronggen · Towards Literate School

Aplikasi publik: https://jelajah-mata-kelas-v-kronggen.breezylobster3.chatgpt.site

## Fitur
Materi dan model mata interaktif, permainan, latihan, uji kompetensi, skor dan pembahasan. Sebelas narasi MP3 berbahasa Indonesia disertakan; tidak memerlukan suara TTS perangkat. Kontrol putar/jeda, berhenti, ulangi, waktu dan kecepatan. Antarmuka responsif untuk sentuhan, mouse dan keyboard.

## Instal dan bagikan
Buka tautan publik lewat Chrome/Edge Android atau komputer, lalu Pasang aplikasi. Pada iPhone/iPad gunakan Safari → Bagikan → Tambah ke Layar Utama. Tunggu status Siap offline sebelum memutus internet. Browser/perangkat harus mendukung PWA; pemasangan bukan APK. Bagikan tautan memakai tombol Bagikan. Hasil siswa tetap di perangkat, tidak dikirim ke server.

## GitHub Pages
Folder docs siap dipublikasikan. Di repository pilih Settings → Pages → Deploy from a branch → main → /docs → Save. GitHub Pages belum diaktifkan oleh pembaruan ini. Gunakan tautan publik di atas sementara itu. Kode di GitHub dan publikasi Sites adalah salinan; perubahan GitHub tidak otomatis menerbitkan Sites.

## Pengembangan
Sajikan folder ini lewat HTTP localhost untuk uji, atau HTTPS untuk produksi. Tidak ada dependensi/build. Ubah versi cache di sw.js setiap mengubah aset agar perangkat mendapat pembaruan. Pembaruan ditawarkan tanpa memutus sesi uji yang sedang berlangsung. Audio dan transkrip ada di audio/. MP3 adalah narasi sintetis Indonesia; teks mengikuti materi aplikasi.

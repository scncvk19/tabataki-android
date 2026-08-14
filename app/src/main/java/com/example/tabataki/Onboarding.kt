package com.example.tabataki

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
    companion object {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }
}

fun getOnboardingString(lang: Language, key: String): String {
    return when (lang) {
        Language.DE -> when(key) {
            "welcome_title" -> "Willkommen bei TABATAKI"
            "welcome_text" -> "Schön, dass du da bist! Bevor wir richtig loslegen, wähle bitte zunächst deine Sprache aus:"
            "support_title" -> "Über dieses Projekt"
            "support_text" -> "Tabataki ist ein persönliches, KI-unterstütztes Lernprojekt. Die App ist werbefrei, funktioniert ohne Benutzerkonto und wird schrittweise weiterentwickelt."
            "tutorial_title" -> "So funktioniert's"
            "tutorial_timer_title" -> "⏱️ Tabata-Timer"
            "tutorial_timer_text" -> "Der klassische Modus. Stelle Arbeitszeit, Pausenzeit und Rundenanzahl manuell ein und starte sofort durch."
            "tutorial_plan_title" -> "📋 Workout-Planer & Bibliothek"
            "tutorial_plan_text" -> "Erstelle eigene Trainingstage und spiele das Workout komplett ab.\n\nHinweis: Übungen, die unter 'Übungen' erstellt werden, bleiben dauerhaft. Übungen, die unter 'Routines' als 'Schnelle Übung' erstellt werden, bleiben nicht dauerhaft und werden nicht gespeichert.\n\nDie Sprache der Übungen ist aus Speichergründen nur auf Englisch begrenzt. Diese kannst du als User manuell abändern oder wie gehabt verwenden. Die restliche App-Oberfläche passt sich aber weiterhin vollautomatisch deiner Sprache an!"
            "privacy_title" -> "100% Datenschutz 🔒"
            "privacy_desc" -> "Uns ist dein Datenschutz extrem wichtig. Aus diesem Grund sammeln wir keinerlei persönliche Trainingsdaten und zwingen dich in keine Cloud."
            "privacy_local_title" -> "Alles Lokal & Sicher"
            "privacy_local_text" -> "Die App läuft komplett offline auf deinem Smartphone. Was auf deinem Handy passiert, bleibt auf deinem Handy!\n\nTIPP: Damit deine Pläne bei einem Handywechsel nicht verloren gehen, nutze einfach die Backup (Export/Import) Funktion im Tab 'Tage'."
            "protips_title" -> "Pro-Tipps 💡"
            "protips_audio_title" -> "🔊 Audio-Feedback"
            "protips_audio_text" -> "Lass das Handy liegen! Tabataki piept in den letzten 3 Sekunden jeder Phase. Du bleibst in der Zone."
            "protips_custom_title" -> "🎾 Uneingeschränkte Freiheit"
            "protips_custom_text" -> "Tabataki passt sich voll und ganz dir an! Alle Kategorien, Übungen und Trainingstage (Routinen) können von dir jederzeit komplett individuell erstellt, bearbeitet oder auch wieder gelöscht werden."
            "protips_battery_title" -> "🔋 Akku & Standby"
            "protips_battery_text" -> "Schließe die App während Workouts nicht. Tabataki hält dein Display automatisch wach, damit der Timer niemals stoppt."
            "protips_dynamic_title" -> "⏱️ Dynamische Workouts"
            "protips_dynamic_text" -> "Mische Zeiten! Zum Beispiel 60s Liegestütze und direkt danach 20s harte Burpees in der gleichen Routine."
            "ready_title" -> "Bereit!"
            "ready_text" -> "Du bist nun bereit für dein erstes Workout mit Tabataki.\n\nViel Spaß und maximalen Erfolg!"
            "btn_back" -> "Zurück"
            "btn_next" -> "Weiter"
            "btn_start" -> "Los geht's!"
            "sel_lang" -> "Sprache wählen:"
            "flex_title" -> "Flexibel & Gestaltbar 🛠️"
            "flex_desc" -> "Tabataki ist kein starres Korsett, sondern dein persönliches Notizbuch!"
            "flex_cat_title" -> "Kategorien & Übungen"
            "flex_cat_text" -> "Erstelle jede Übung und Kategorie (z.B. 'Tennis') genau so, wie du sie brauchst. Du kannst sie über das Zahnrad bearbeiten oder über das X-Symbol komplett löschen."
            "flex_plan_title" -> "Tage & Routinen"
            "flex_plan_text" -> "Passe deine Trainingstage beliebig an. Füge Übungen hinzu, ändere die Reihenfolge oder lösche den ganzen Tag, falls du ihn nicht mehr brauchst."
            else -> key
        }
        Language.ES -> when(key) {
            "welcome_title" -> "Bienvenido a TABATAKI"
            "welcome_text" -> "¡Qué bueno tenerte aquí! Antes de empezar, selecciona tu idioma:"
            "support_title" -> "Sobre este proyecto"
            "support_text" -> "Tabataki es un proyecto personal de aprendizaje desarrollado con ayuda de IA. La aplicación no contiene publicidad, funciona sin cuenta y se mejora paso a paso."
            "tutorial_title" -> "Cómo funciona"
            "tutorial_timer_title" -> "⏱️ Temporizador Tabata"
            "tutorial_timer_text" -> "El modo clásico. Configura manualmente el tiempo de trabajo, descanso y rondas, y empieza."
            "tutorial_plan_title" -> "📋 Planificador y Biblioteca"
            "tutorial_plan_text" -> "Crea tus días de entrenamiento y reproduce toda tu rutina.\n\nNota: Los ejercicios creados en 'Ejercicios' se guardan permanentemente. Los creados en 'Rutinas' como 'Ejercicio Rápido' no se guardan permanentemente.\n\nPor razones de memoria, el idioma de los ejercicios de origen se limita al inglés. Puedes modificarlos manualmente o usarlos como están. ¡El resto de la interfaz de la app se traduce automáticamente a tu idioma!"
            "privacy_title" -> "100% Privacidad 🔒"
            "privacy_desc" -> "Tu privacidad es extremadamente importante. Por eso no recopilamos datos de entrenamiento ni forzamos inicio de sesión en la nube."
            "privacy_local_title" -> "Local y Seguro"
            "privacy_local_text" -> "La app funciona completamente offline. Lo que pasa en tu teléfono, se queda en tu teléfono.\n\nCONSEJO: Usa la función de Copia de Seguridad (Exportar/Importar) en 'Días' para no perder tus rutinas al cambiar de teléfono."
            "protips_title" -> "Consejos Pro 💡"
            "protips_audio_title" -> "🔊 Audio Feedback"
            "protips_audio_text" -> "¡Deja el teléfono! Tabataki pita en los últimos 3 segundos de cada fase."
            "protips_custom_title" -> "🎾 Libertad Total"
            "protips_custom_text" -> "¡Tabataki se adapta totalmente a ti! Todas las categorías, ejercicios y días de entrenamiento (rutinas) pueden ser creados, editados o eliminados de forma individual por ti en cualquier momento."
            "protips_battery_title" -> "🔋 Batería y Espera"
            "protips_battery_text" -> "No cierres la app durante el ejercicio. Tabataki mantiene la pantalla encendida para que el temporizador nunca se detenga."
            "protips_dynamic_title" -> "⏱️ Entrenamientos Dinámicos"
            "protips_dynamic_text" -> "¡Mezcla tus tiempos! Por ejemplo, 60s de flexiones seguidos directamente de 20s de burpees duros en la misma rutina."
            "ready_title" -> "¡Listo!"
            "ready_text" -> "Ya estás listo para tu primer entrenamiento con Tabataki.\n\n¡Diviértete y mucho éxito!"
            "btn_back" -> "Atrás"
            "btn_next" -> "Siguiente"
            "btn_start" -> "¡Vamos!"
            "sel_lang" -> "Seleccionar idioma:"
            "flex_title" -> "Flexible y Personalizable 🛠️"
            "flex_desc" -> "¡Tabataki no es un corsé rígido, sino tu cuaderno personal!"
            "flex_cat_title" -> "Categorías y Ejercicios"
            "flex_cat_text" -> "Crea cualquier ejercicio y categoría (ej. 'Tenis') exactamente como lo necesites. Edítalos a través del icono de engranaje o elimínalos por completo con el símbolo X."
            "flex_plan_title" -> "Días y Rutinas"
            "flex_plan_text" -> "Adapta libremente tus días de entrenamiento. Añade ejercicios, cambia el orden o elimina todo el día si ya no lo necesitas."
            else -> key
        }
        Language.FR -> when(key) {
            "welcome_title" -> "Bienvenue sur TABATAKI"
            "welcome_text" -> "Ravi de vous voir ! Avant de commencer, veuillez sélectionner votre langue :"
            "support_title" -> "À propos du projet"
            "support_text" -> "Tabataki est un projet d’apprentissage personnel développé avec l’aide de l’IA. L’application est sans publicité, fonctionne sans compte et évolue progressivement."
            "tutorial_title" -> "Comment ça marche"
            "tutorial_timer_title" -> "⏱️ Minuteur Tabata"
            "tutorial_timer_text" -> "Le mode classique. Réglez manuellement le travail, le repos et les séries, puis commencez."
            "tutorial_plan_title" -> "📋 Planificateur et Bibliothèque"
            "tutorial_plan_text" -> "Créez vos jours d'entraînement et lancez votre routine entière.\n\nNote : Les exercices créés dans 'Exercices' sont permanents. Les 'Exercices Rapides' créés dans les Routines sont temporaires.\n\nPour économiser de l'espace, les exercices fournis sont limités à l'anglais. Vous pouvez les modifier ou les utiliser tels quels. Le reste de l'interface est traduit automatiquement dans votre langue !"
            "privacy_title" -> "100% Confidentialité 🔒"
            "privacy_desc" -> "Votre confidentialité est essentielle. Nous ne collectons aucune donnée et n'imposons aucun compte cloud."
            "privacy_local_title" -> "Local et Sécurisé"
            "privacy_local_text" -> "L'application fonctionne hors ligne. Ce qui se passe sur votre téléphone reste sur votre téléphone.\n\nCONSEIL : Utilisez la fonctionnalité de Sauvegarde (Export/Import) dans 'Jours' pour ne pas perdre vos données en changeant de téléphone."
            "protips_title" -> "Astuces Pro 💡"
            "protips_audio_title" -> "🔊 Retour Audio"
            "protips_audio_text" -> "Posez le téléphone ! Tabataki bipe lors des 3 dernières secondes de chaque phase."
            "protips_custom_title" -> "🎾 Liberté Totale"
            "protips_custom_text" -> "Tabataki s'adapte entièrement à vous ! Toutes les catégories, exercices et jours d'entraînement (routines) peuvent être créés, modifiés ou supprimés individuellement par vous à tout moment."
            "protips_battery_title" -> "🔋 Batterie & Veille"
            "protips_battery_text" -> "Ne fermez pas l'application pendant l'entraînement. Tabataki garde l'écran allumé pour que le minuteur ne s'arrête jamais."
            "protips_dynamic_title" -> "⏱️ Entraînements Dynamiques"
            "protips_dynamic_text" -> "Mélangez vos temps ! (ex: 60s de pompes suivies de 20s de burpees dans la même routine)."
            "ready_title" -> "Prêt !"
            "ready_text" -> "Vous êtes prêt pour votre premier entraînement avec Tabataki.\n\nAmusez-vous bien et bon courage !"
            "btn_back" -> "Retour"
            "btn_next" -> "Suivant"
            "btn_start" -> "C'est parti !"
            "sel_lang" -> "Choisir la langue :"
            "flex_title" -> "Flexible & Personnalisable 🛠️"
            "flex_desc" -> "Tabataki n'est pas un corset rigide, mais votre carnet personnel !"
            "flex_cat_title" -> "Catégories & Exercices"
            "flex_cat_text" -> "Créez chaque exercice et catégorie (ex. 'Tennis') exactement comme vous le souhaitez. Modifiez-les via l'icône d'engrenage ou supprimez-les définitivement avec la croix."
            "flex_plan_title" -> "Jours & Routines"
            "flex_plan_text" -> "Adaptez librement vos jours d'entraînement. Ajoutez des exercices, modifiez l'ordre ou supprimez le jour entier si vous n'en avez plus besoin."
            else -> key
        }
        Language.IT -> when(key) {
            "welcome_title" -> "Benvenuto su TABATAKI"
            "welcome_text" -> "Felici di averti qui! Prima di iniziare, seleziona la tua lingua:"
            "support_title" -> "Informazioni sul progetto"
            "support_text" -> "Tabataki è un progetto personale di apprendimento sviluppato con il supporto dell’IA. L’app è senza pubblicità, funziona senza account e viene migliorata gradualmente."
            "tutorial_title" -> "Come funziona"
            "tutorial_timer_title" -> "⏱️ Timer Tabata"
            "tutorial_timer_text" -> "La modalità classica. Imposta manualmente il tempo di lavoro, il riposo e i round, quindi inizia."
            "tutorial_plan_title" -> "📋 Pianificatore & Libreria"
            "tutorial_plan_text" -> "Crea i tuoi giorni di allenamento e avvia l'intera routine.\n\nNota: Gli esercizi creati in 'Esercizi' sono permanenti. Gli 'Esercizi Rapidi' creati nelle Routine non vengono salvati.\n\nPer motivi di memoria, gli esercizi integrati sono limitati all'inglese. Puoi modificarli o usarli così come sono. Il resto dell'interfaccia si adatta automaticamente alla tua lingua!"
            "privacy_title" -> "100% Privacy 🔒"
            "privacy_desc" -> "La tua privacy è estremamente importante. Non raccogliamo dati di allenamento né forziamo il login nel cloud."
            "privacy_local_title" -> "Locale e Sicuro"
            "privacy_local_text" -> "L'app funziona completamente offline. Ciò che succede sul tuo telefono, rimane sul tuo telefono!\n\nSUGGERIMENTO: Usa la funzione Backup (Esporta/Importa) nella scheda 'Giorni' per non perdere i tuoi piani."
            "protips_title" -> "Suggerimenti Pro 💡"
            "protips_audio_title" -> "🔊 Audio Feedback"
            "protips_audio_text" -> "Metti giù il telefono! Tabataki emette un segnale acustico negli ultimi 3 secondi di ogni fase."
            "protips_custom_title" -> "🎾 Libertà Totale"
            "protips_custom_text" -> "Tabataki si adatta completamente a te! Tutte le categorie, gli esercizi e i giorni di allenamento (routine) possono essere creati, modificati o eliminati individualmente da te in qualsiasi momento."
            "protips_battery_title" -> "🔋 Batteria e Standby"
            "protips_battery_text" -> "Non chiudere l'app durante gli allenamenti. Tabataki mantiene lo schermo acceso automaticamente."
            "protips_dynamic_title" -> "⏱️ Allenamenti Dinamici"
            "protips_dynamic_text" -> "Mischia i tuoi tempi! Ad esempio, 60s di flessioni seguite da 20s di burpees tosti nella stessa routine."
            "ready_title" -> "Pronto!"
            "ready_text" -> "Ora sei pronto per il tuo primo allenamento con Tabataki.\n\nBuon divertimento e buon allenamento!"
            "btn_back" -> "Indietro"
            "btn_next" -> "Avanti"
            "btn_start" -> "Iniziamo!"
            "sel_lang" -> "Seleziona lingua:"
            "flex_title" -> "Flessibile e Personalizzabile 🛠️"
            "flex_desc" -> "Tabataki non è un sistema rigido, ma il tuo quaderno personale!"
            "flex_cat_title" -> "Categorie ed Esercizi"
            "flex_cat_text" -> "Crea qualsiasi esercizio e categoria (es. 'Tennis') esattamente come ne hai bisogno. Modificali quando vuoi tramite l'icona dell'ingranaggio o eliminali completamente con la X."
            "flex_plan_title" -> "Giorni e Routine"
            "flex_plan_text" -> "Adatta liberamente i tuoi giorni di allenamento. Aggiungi esercizi, cambia l'ordine o elimina l'intero giorno se non ti serve più."
            else -> key
        }
        Language.TR -> when(key) {
            "welcome_title" -> "TABATAKI'ye Hoş Geldiniz"
            "welcome_text" -> "Sizi burada görmek harika! Başlamadan önce lütfen dilinizi seçin:"
            "support_title" -> "Bu proje hakkında"
            "support_text" -> "Tabataki, yapay zekâ desteğiyle geliştirilen kişisel bir öğrenme projesidir. Uygulama reklamsızdır, hesap gerektirmez ve adım adım geliştirilmektedir."
            "tutorial_title" -> "Nasıl çalışır"
            "tutorial_timer_title" -> "⏱️ Tabata Sayacı"
            "tutorial_timer_text" -> "Klasik mod. Çalışma, dinlenme ve tur sayılarını manuel ayarlayın."
            "tutorial_plan_title" -> "📋 Egzersiz Planlayıcı & Kütüphane"
            "tutorial_plan_text" -> "Kendi eğitim günlerinizi oluşturun ve tüm rutininizi oynatın.\n\nNot: 'Egzersizler' altında oluşturulan egzersizler kalıcıdır. Rutinlerdeki 'Hızlı Egzersiz'ler kalıcı değildir ve kaydedilmez.\n\nBellek tasarrufu için egzersiz dili İngilizce ile sınırlıdır. Bunları manuel olarak değiştirebilir veya olduğu gibi kullanabilirsiniz. Uygulamanın geri kalanı tamamen kendi dilinize çevrilir!"
            "privacy_title" -> "%100 Gizlilik 🔒"
            "privacy_desc" -> "Gizliliğiniz bizim için çok önemli. Egzersiz verisi toplamıyoruz ve cloud oturumu zorunlu değil."
            "privacy_local_title" -> "Yerel ve Güvenli"
            "privacy_local_text" -> "Uygulama tamamen çevrimdışı çalışır. Telefonunuzda olan, telefonunuzda kalır!\n\nİPUCU: Planlarınızı kaybetmemek için 'Günler' sekmesindeki Yedekleme (Dışa Aktar/İçe Aktar) özelliğini kullanın."
            "protips_title" -> "Pro İpuçları 💡"
            "protips_audio_title" -> "🔊 Sesli Geri Bildirim"
            "protips_audio_text" -> "Telefonunuzu bırakın! Tabataki her aşamanın son 3 saniyesinde bipleme sesi çıkarır."
            "protips_custom_title" -> "🎾 Tam Özgürlük"
            "protips_custom_text" -> "Tabataki tamamen sana uyarlıdır! Tüm kategoriler, egzersizler ve eğitim günleri (rutinler) istediğin zaman senin tarafından tamamen bireysel olarak oluşturulabilir, düzenlenebilir veya silinebilir."
            "protips_battery_title" -> "🔋 Pil ve Bekleme"
            "protips_battery_text" -> "Egzersizler sırasında uygulamayı kapatmayın. Sayaç hiç durmasın diye ekranı otomatik olarak uyanık tutar."
            "protips_dynamic_title" -> "⏱️ Dinamik Egzersizler"
            "protips_dynamic_text" -> "Sürelerinizi karıştırın! Mesela 60 saniye şınav, ardından 20 saniye burpee."
            "ready_title" -> "Hazır!"
            "ready_text" -> "Tabataki ile ilk antrenmanınıza artık hazırsınız.\n\nİyi eğlenceler ve başarılar!"
            "btn_back" -> "Geri"
            "btn_next" -> "İleri"
            "btn_start" -> "Başla!"
            "sel_lang" -> "Dil Seçin:"
            "flex_title" -> "Esnek & Özelleştirilebilir 🛠️"
            "flex_desc" -> "Tabataki katı bir korse değil, senin kişisel defterindir!"
            "flex_cat_title" -> "Kategoriler & Egzersizler"
            "flex_cat_text" -> "Her egzersiz ve kategoriyi (örn. 'Tenis') tam ihtiyacın olduğu gibi yarat. Çark ikonuyla düzenle veya X sembolüyle tamamen sil."
            "flex_plan_title" -> "Günler & Rutinler"
            "flex_plan_text" -> "Eğitim günlerini özgürce uyarla. Egzersiz ekle, sırayı değiştir veya artık ihtiyacın yoksa günü tamamen sil."
            else -> key
        }
        Language.RU -> when(key) {
            "welcome_title" -> "Добро пожаловать в TABATAKI"
            "welcome_text" -> "Рады видеть вас здесь! Перед началом выберите язык:"
            "support_title" -> "О проекте"
            "support_text" -> "Tabataki — персональный учебный проект, созданный при поддержке ИИ. Приложение работает без рекламы и учётной записи и постепенно развивается."
            "tutorial_title" -> "Как это работает"
            "tutorial_timer_title" -> "⏱️ Таймер Табата"
            "tutorial_timer_text" -> "Классический режим. Установите время работы, отдыха и круги."
            "tutorial_plan_title" -> "📋 Планировщик & Библиотека"
            "tutorial_plan_text" -> "Создавайте дни тренировок и запускайте программу целиком.\n\nПримечание: Упражнения, созданные в 'Упражнениях', сохраняются навсегда. 'Быстрые упражнения' в программах — временные.\n\nВ целях экономии памяти, язык упражнений ограничен английским. Вы можете вручную изменить их. Остальная часть интерфейса приложения переводится на ваш язык автоматически!"
            "privacy_title" -> "100% Конфиденциальность 🔒"
            "privacy_desc" -> "Мы не собираем тренировочные данные и не заставляем вас входить в облако."
            "privacy_local_title" -> "Локально & Безопасно"
            "privacy_local_text" -> "Приложение работает полностью оффлайн. Что происходит в телефоне — остается в телефоне!\n\nСОВЕТ: Используйте функцию резервного копирования (Экспорт/Импорт), чтобы не потерять данные при смене телефона."
            "protips_title" -> "Про-советы 💡"
            "protips_audio_title" -> "🔊 Звуки"
            "protips_audio_text" -> "Tabataki подает сигнал за 3 секунды до конца каждой фазы. Сосредоточьтесь на тренировке!"
            "protips_custom_title" -> "🎾 Полная Свобода"
            "protips_custom_text" -> "Tabataki полностью адаптируется под вас! Все категории, упражнения и дни тренировок (программы) могут быть созданы, изменены или удалены вами самостоятельно в любое время."
            "protips_battery_title" -> "🔋 Батарея"
            "protips_battery_text" -> "Не закрывайте приложение во время работы. Tabataki держит экран включенным."
            "protips_dynamic_title" -> "⏱️ Динамичные Тренировки"
            "protips_dynamic_text" -> "Смешивайте время! Например, 60 сек отжиманий и 20 сек берпи в одной программе."
            "ready_title" -> "Готово!"
            "ready_text" -> "Теперь вы готовы к своей первой тренировке.\n\nУдачи и отличных результатов!"
            "btn_back" -> "Назад"
            "btn_next" -> "Далее"
            "btn_start" -> "Начнем!"
            "sel_lang" -> "Выберите язык:"
            "flex_title" -> "Гибкость и Настройка 🛠️"
            "flex_desc" -> "Tabataki - это не жесткий корсет, а ваш личный блокнот!"
            "flex_cat_title" -> "Категории и Упражнения"
            "flex_cat_text" -> "Создавайте любые упражнения и категории (например, 'Теннис') именно так, как вам нужно. Редактируйте их в любое время или полностью удаляйте через значок X."
            "flex_plan_title" -> "Дни и Программы"
            "flex_plan_text" -> "Свободно адаптируйте свои дни тренировок. Добавляйте упражнения, меняйте порядок или удаляйте весь день целиком."
            else -> key
        }
        else -> when(key) { // Default to English for all other languages initially
            "welcome_title" -> "Welcome to TABATAKI"
            "welcome_text" -> "Great to have you here! Before we start, please select your language:"
            "support_title" -> "About this project"
            "support_text" -> "Tabataki is a personal learning project developed with AI assistance. The app is ad-free, works without an account, and is being improved step by step."
            "tutorial_title" -> "How it works"
            "tutorial_timer_title" -> "⏱️ Tabata Timer"
            "tutorial_timer_text" -> "The classic mode. Set your work time, rest time, and rounds manually and start immediately."
            "tutorial_plan_title" -> "📋 Workout Planner & Library"
            "tutorial_plan_text" -> "Create your own training days and play your complete workout.\n\nNote: Exercises created under 'Exercises' remain permanently. Exercises created under 'Routines' as a 'Quick Exercise' do not remain permanently and are not saved.\n\nThe language of the exercises is limited to English for memory reasons. You can manually change these as a user or use them as is. The rest of the app's interface still strictly adapts to your selected language!"
            "privacy_title" -> "100% Privacy 🔒"
            "privacy_desc" -> "Your privacy is extremely important to us. That's why we do not collect any training data and do not force a cloud login."
            "privacy_local_title" -> "Local & Secure"
            "privacy_local_text" -> "The app runs completely offline on your smartphone. What happens on your phone, stays on your phone!\n\nTIP: To avoid losing your plans when switching phones, just use the Backup (Export/Import) feature in the 'Days' tab."
            "protips_title" -> "Pro Tips 💡"
            "protips_audio_title" -> "🔊 Audio Feedback"
            "protips_audio_text" -> "Put your phone down! Tabataki beeps during the last 3 seconds of every phase. Stay in the zone."
            "protips_custom_title" -> "🎾 Ultimate Freedom"
            "protips_custom_text" -> "Tabataki adapts entirely to you! All categories, exercises, and training days (routines) can be completely individually created, edited, or deleted by you at any time."
            "protips_battery_title" -> "🔋 Battery & Standby"
            "protips_battery_text" -> "Do not close the app during workouts. Tabataki automatically keeps your screen awake so the timer never stops."
            "protips_dynamic_title" -> "⏱️ Dynamic Workouts"
            "protips_dynamic_text" -> "Mix your times! E.g. 60s of Push-ups followed directly by 20s of hard Burpees in the same routine."
            "ready_title" -> "Ready!"
            "ready_text" -> "You are now ready for your first workout with Tabataki.\n\nHave fun and enjoy!"
            "btn_back" -> "Back"
            "btn_next" -> "Next"
            "btn_start" -> "Let's Go!"
            "sel_lang" -> "Select Language:"
            "flex_title" -> "Flexible & Customizable 🛠️"
            "flex_desc" -> "Tabataki is not a rigid corset, but your personal notebook!"
            "flex_cat_title" -> "Categories & Exercises"
            "flex_cat_text" -> "Create any exercise and category (e.g., 'Tennis') exactly how you need it. Edit them anytime via the gear icon or delete them completely using the X symbol."
            "flex_plan_title" -> "Days & Routines"
            "flex_plan_text" -> "Freely adapt your training days. Add exercises, change the order, or delete the whole day if you no longer need it."
            else -> key
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    tabataState: TabataState,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 7 })
    val coroutineScope = rememberCoroutineScope()

    val lang = tabataState.appLang

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> WelcomePage(tabataState, lang)
                1 -> SupportPage(lang)
                2 -> TutorialPage(lang)
                3 -> CustomizationPage(lang)
                4 -> PrivacyPage(lang)
                5 -> ProTipsPage(lang)
                6 -> ReadyPage(lang)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pager Indicators
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(7) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage > 0) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Text(getOnboardingString(lang, "btn_back"), color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Spacer(modifier = Modifier.width(64.dp))
            }

            if (pagerState.currentPage < 6) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(getOnboardingString(lang, "btn_next"), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            } else {
                Button(
                    onClick = onFinish,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(getOnboardingString(lang, "btn_start"), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProTipsPage(lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = getOnboardingString(lang, "protips_title"),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "protips_audio_title"), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text(getOnboardingString(lang, "protips_audio_text"), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "protips_custom_title"), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text(getOnboardingString(lang, "protips_custom_text"), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "protips_battery_title"), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text(getOnboardingString(lang, "protips_battery_text"), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "protips_dynamic_title"), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(4.dp))
                Text(getOnboardingString(lang, "protips_dynamic_text"), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun WelcomePage(tabataState: TabataState, lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(getOnboardingString(lang, "welcome_title"), fontSize = 32.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            getOnboardingString(lang, "welcome_text"),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(getOnboardingString(lang, "sel_lang"), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        var langExpanded by remember { mutableStateOf(false) }
        Box {
            Button(
                onClick = { langExpanded = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Text(tabataState.appLang.name + " ▼")
            }
            DropdownMenu(expanded = langExpanded, onDismissRequest = { langExpanded = false }, modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                Language.values().forEach { l ->
                    DropdownMenuItem(
                        text = { Text(l.name, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = { 
                            tabataState.setLanguage(l)
                            langExpanded = false 
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SupportPage(lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(getOnboardingString(lang, "support_title"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            getOnboardingString(lang, "support_text"),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TutorialPage(lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(getOnboardingString(lang, "tutorial_title"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "tutorial_timer_title"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text(getOnboardingString(lang, "tutorial_timer_text"), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "tutorial_plan_title"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text(getOnboardingString(lang, "tutorial_plan_text"), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun PrivacyPage(lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(getOnboardingString(lang, "privacy_title"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            getOnboardingString(lang, "privacy_desc"),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(getOnboardingString(lang, "privacy_local_title"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.height(8.dp))
                Text(getOnboardingString(lang, "privacy_local_text"), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}

@Composable
fun ReadyPage(lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(getOnboardingString(lang, "ready_title"), fontSize = 36.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            getOnboardingString(lang, "ready_text"),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CustomizationPage(lang: Language) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(getOnboardingString(lang, "flex_title"), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            getOnboardingString(lang, "flex_desc"),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "flex_cat_title"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text(getOnboardingString(lang, "flex_cat_text"), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(getOnboardingString(lang, "flex_plan_title"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text(getOnboardingString(lang, "flex_plan_text"), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

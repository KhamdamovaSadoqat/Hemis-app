package com.software.hemis.database.room

import androidx.room.Database
import androidx.room.TypeConverters
import com.software.hemis.domain.main.attendance.AttendanceEntity
import com.software.hemis.domain.main.profile.ProfileEntity
import com.software.hemis.domain.main.resources.ResourceEntity
import com.software.hemis.domain.main.schedule.ScheduleEntity
import com.software.hemis.domain.main.semester.SemesterEntity
import com.software.hemis.domain.main.semester.WeekEntity
import com.software.hemis.domain.main.subject.SubjectEntity
import com.software.hemis.domain.main.subjectDetails.SubjectDetailsEntity
import com.software.hemis.domain.main.task.TaskDetailEntity
import com.software.hemis.domain.main.task.TaskEntity
import com.software.hemis.domain.main.task.TaskSentSubmissionEntity

@Database(
    entities = [SemesterEntity::class, SubjectEntity::class,
        ScheduleEntity::class, WeekEntity::class, ProfileEntity::class,
        SubjectDetailsEntity::class, TaskEntity::class, TaskDetailEntity::class,
        TaskSentSubmissionEntity::class, ResourceEntity::class,
        AttendanceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class RoomDatabase : androidx.room.RoomDatabase() {

    abstract val semesterDao: SemesterDao
    abstract val subjectDao: SubjectDao
    abstract val scheduleDao: ScheduleDao
    abstract val weekDao: WeekDao
    abstract val profileDao: ProfileDao
    abstract val subjectDetailsDao: SubjectDetailsDao
    abstract val taskDao: TaskDao
    abstract val taskDetailDao: TaskDetailDao
    abstract val resourceDao: ResourceDao
    abstract val attendanceDao: AttendanceDao
}


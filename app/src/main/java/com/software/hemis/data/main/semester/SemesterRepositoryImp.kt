package com.software.hemis.data.main.semester

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.SemesterDao
import com.software.hemis.database.room.WeekDao
import com.software.hemis.domain.base.BaseResultList
import com.software.hemis.domain.main.semester.SemesterEntity
import com.software.hemis.domain.main.semester.SemesterRepository
import com.software.hemis.domain.main.semester.WeekEntity
import com.software.hemis.utils.data.SharedPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SemesterRepositoryImp @Inject constructor(
    private val semesterApi: SemesterApi,
    private val pref: SharedPref,
    private val semesterDao: SemesterDao,
    private val weekDao: WeekDao,
) : SemesterRepository {
    override suspend fun semester(): Flow<BaseResultList<List<SemesterEntity>, List<WeekEntity>, WrappedResponse<SemesterResponse>>> {
        return flow {
            val response = semesterApi.semester()
            // checking data for success and not being null
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // checking body data for not being null
                if (body.data != null) {
                    // as it is not null using body.data!! is accessible
                    val listSemesterEntity = arrayListOf<SemesterEntity>()
                    val listWeekEntity = arrayListOf<WeekEntity>()
                    // getting both: semester list and week list of current semester
                    for (semester in body.data!!.indices) {
                        if (body.data!![semester].current == true) {
                            pref.currentSemester = body.data!![semester].code?.toInt()
                            pref.currentSemesterName = body.data!![semester].name
                            pref.weekMinId = body.data!![semester].weeks!![0]?.id
                            pref.weekMaxId = body.data!![semester].weeks!!.last()!!.id

                            // getting alllllll week that have for 1 semesters: it is approx 25
                            if (body.data!![semester].weeks != null)
                                for (week in body.data!![semester].weeks!!.indices) {
                                    if (body.data!![semester].weeks!![week]?.current == true)
                                        pref.currentWeek = body.data!![semester].weeks!![week]?.id
                                    // sometimes current week comes always false in current semester so
                                    // taking current week as default last one
                                    else {
                                        if (pref.currentWeek == null)
                                            pref.currentWeek =
                                                body.data!![semester].weeks?.last()?.id
                                    }

                                    val weekEntity = WeekEntity(
                                        body.data!![semester].weeks!![week]?.id ?: -1,
                                        body.data!![semester].weeks!![week]?.semester ?: "",
                                        body.data!![semester].weeks!![week]?.current ?: false,
                                        body.data!![semester].weeks!![week]?.startDate ?: 0,
                                        body.data!![semester].weeks!![week]?.endDate ?: 0,
                                    )
                                    listWeekEntity.add(weekEntity)
                                }
                            else {
                                pref.weekMinId = 0
                                pref.weekMaxId = 0
                                pref.currentWeek = 0
                            }
                        }
                        val semesterEntity = SemesterEntity(
                            body.data!![semester].id ?: -1,
                            body.data!![semester].code ?: "",
                            body.data!![semester].name ?: "",
                            body.data!![semester].current ?: false,
                            body.data!![semester].educationYear?.name ?: "",
                            body.data!![semester].educationYear?.code ?: "",
                            body.data!![semester].educationYear?.current ?: false
                        )
                        listSemesterEntity.add(semesterEntity)
                    }

                    if (pref.currentSemester == 0) {
                        pref.currentSemester = body.data!![0].code?.toInt()
                        pref.currentSemesterName = body.data!![0].name
                        if (body.data!![0].weeks!!.isEmpty()) {
                            pref.weekMinId = 0
                            pref.weekMaxId = 0
                            pref.currentWeek = 0
                        }
                    }
                    Log.d("-------------", "semester: currentWeek: ${pref.currentWeek}")
                    Log.d("-------------", "semester: weekMin: ${pref.weekMinId}")
                    Log.d("-------------", "semester: weekMax: ${pref.weekMaxId}")
                    Log.d("-------------", "semester: currentSemester: ${pref.currentSemester}")
                    emit(BaseResultList.Success(listSemesterEntity, listWeekEntity))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<SemesterResponse>>() {}.type
                val err: WrappedResponse<SemesterResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResultList.Error(err))
            }
        }
    }

    override suspend fun insertSemester(semesterEntity: SemesterEntity) {
        semesterDao.insertSemester(semesterEntity)
    }

    override suspend fun insertWeek(weekEntity: WeekEntity) {
        weekDao.insertWeek(weekEntity)
    }

    override suspend fun getSemester(): LiveData<List<SemesterEntity>> {
        return semesterDao.getSemester()
    }

    override suspend fun getCurrentSemester(currentSemester: Int): LiveData<SemesterEntity> {
        return semesterDao.getCurrentSemester(currentSemester)
    }
}
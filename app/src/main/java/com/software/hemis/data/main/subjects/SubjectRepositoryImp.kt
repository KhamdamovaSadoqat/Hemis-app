package com.software.hemis.data.main.subjects

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.software.hemis.data.comman.WrappedResponse
import com.software.hemis.database.room.SubjectDao
import com.software.hemis.domain.base.BaseResult
import com.software.hemis.domain.main.subject.SubjectEntity
import com.software.hemis.domain.main.subject.SubjectRepository
import com.software.hemis.domain.main.subject.SubjectWithSubjectDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubjectRepositoryImp @Inject constructor(
    private val subjectApi: SubjectApi,
    private val subjectDao: SubjectDao,
) : SubjectRepository {

    override suspend fun subjects(): Flow<BaseResult<List<SubjectEntity>, WrappedResponse<SubjectResponse>>> {
        return flow {
            val response = subjectApi.subjects()
            if (response.body()!!.success && response.body()?.data != null) {
                val body = response.body()!!
                val listSubjectEntity = arrayListOf<SubjectEntity>()
                for (subject in body.data?.indices!!) {
                    val subjectEntity = SubjectEntity(
                        body.data!![subject].subject?.name ?: "",
                        body.data!![subject].subject?.id ?: -1,
                        body.data!![subject].subjectType?.name ?: "",
                        body.data!![subject].subjectType?.code ?: "",
                        body.data!![subject].semester?.toInt() ?: -1,
                        body.data!![subject].totalAcload ?: -1,
                        body.data!![subject].credit ?: -1
                    )
                    listSubjectEntity.add(subjectEntity)
                }
                emit(BaseResult.Success(listSubjectEntity))
            } else {
                val type = object : TypeToken<WrappedResponse<SubjectResponse>>() {}.type
                val err: WrappedResponse<SubjectResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun insertSubject(subjectEntity: SubjectEntity) {
        subjectDao.insertSubject(subjectEntity)
    }

    override suspend fun getSubjectList(semesterCode: Int): LiveData<List<SubjectEntity>> {
        return subjectDao.getSubjectList(semesterCode)
    }

    override suspend fun getSubject(subjectId: Int): LiveData<SubjectEntity> {
        return  subjectDao.getSubject(subjectId)
    }

    override suspend fun getSubjectWithSubjectDetails(semesterCode: Int): LiveData<List<SubjectWithSubjectDetails>> {
        return  subjectDao.getSubjectWithSubjectDetails(semesterCode)
    }
}
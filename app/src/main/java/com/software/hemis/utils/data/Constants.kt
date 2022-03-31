package com.software.hemis.utils.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.hemis.R
import com.software.hemis.presenter.auth.introduction.Intro

object Constants {

    var baseUrl = "https://student.hemis.uz/rest/v1/"
    var universityUrl = ""

    const val helpTypeSubject = 1
    const val helpTypeSchedule = 2
    const val helpTypeDeadline = 4

    const val BUNDLE_POSITION = "bundle_position"
    const val BUNDLE_SUBJECT_ID = "bundle_subject_id"

    const val WAITING_UPLOAD = 0
    const val LESS_THAN_THREE_DAYS = 1
    const val TASK_UPLOADED = 2
    const val TASK_MARKED = 3
    const val DEADLINE_PASSED = 4
    const val NEW_TASK = 5
    const val ALL = 6

    const val LESS_THAN_TWO_DAYS = 7
    const val LESS_THAN_FOUR_DAYS = 8
    const val LESS_THAN_FIVE_DAYS = 9
    const val LESS_THAN_SIX_DAYS = 10
    const val LESS_THAN_SEVEN_DAYS = 11


    const val GIVEN = 11
    const val UPLOADED = 12
    const val MARKED = 13

    const val LECTURE = 11
    const val LABORATORY = 12
    const val PRACTICAL = 13
    const val SEMINAR = 14
    const val TRAINING = 15
    const val COURSE_WORK = 16
    const val INDEPENDENT_WORK = 17

    const val LECTURE_PAGE = 0
    const val LABORATORY_PAGE = 1
    const val PRACTICAL_PAGE = 2
    const val SEMINAR_PAGE = 3
    const val TRAINING_PAGE = 4
    const val COURSE_WORK_PAGE = 5
    const val INDEPENDENT_WORK_PAGE = 6

    const val INTENT_GET_DOCUMENT = 1001

    const val FILE_STORE_PACKAGE = "/storage/emulated/0/Download"

    fun getIntro(): ArrayList<Intro> {
        return arrayListOf(
            Intro(R.drawable.ic_ombording_1, R.string.hemis, R.string.sub_header_1),
            Intro(R.drawable.ic_ombording_2, R.string.fanlar, R.string.sub_header_2),
            Intro(R.drawable.ic_ombording_3, R.string.bildirishnomalar, R.string.sub_header_3),
            Intro(R.drawable.ic_ombording_4, R.string.malumotlar, R.string.sub_header_4)
        )
    }

    fun getMonth(): ArrayList<Int> {
        return arrayListOf(
            R.string.yanvar,
            R.string.fevral,
            R.string.mart,
            R.string.aprel,
            R.string.may,
            R.string.iyun,
            R.string.iyul,
            R.string.avgust,
            R.string.sentabr,
            R.string.oktabr,
            R.string.noyabr,
            R.string.dekabr
        )
    }

    fun getHelp(helpType: Int): ArrayList<String> {
        when (helpType) {
            1 -> {
                return arrayListOf("Fan nomi",
                    "Fanning turi | semester davomidagi darslar soati | kredit qiymati",
                    "Topshirilgan topshiriqlar soni va umumiy topshiriqlar soni",
                    "Fanning tugallanganlik indikatori",
                    "Fan haqida batafsil ma'lumot olish tugmasi")
            }
            2 -> {
                return arrayListOf("Fan nomi",
                    "Dars boshlanish vaqti",
                    "Dars tugash vaqti",
                    "Auditoriya raqami",
                    "O'qituvchining ismi",
                    "Dars turi")
            }
            3 -> {
                return arrayListOf("Fan nomi",
                    "O'qituvchining ismi",
                    "Fan bo'yicha qoldirilgan soatlar qiymati",
                    "Dars qoldirilgan kun",
                    "Dars turi")
            }
            4 -> {
                return arrayListOf("Topshiriq nomi",
                    "Fan nomi",
                    "Topshiriq statusi",
                    "Topshiriq topshirilishi kutilayotgan sana",
                    "Fan turi",
                    "Batafsil ma'lumot")
            }
        }
        return arrayListOf()
    }

    fun getHelpPic(helpType: Int, context: Context): Drawable?{
        when(helpType){
            1 -> return ContextCompat.getDrawable(context, R.drawable.img_help_1)
            2 -> return ContextCompat.getDrawable(context, R.drawable.ic_help_2)
            3 -> return ContextCompat.getDrawable(context, R.drawable.ic_help_3)
            4 -> return ContextCompat.getDrawable(context, R.drawable.ic_help_4)
        }
        return  null
    }

    fun loadResourceTitle(context: Context) = listOf(
        context.getString(R.string.type_lecture),
        context.getString(R.string.type_laboratory),
        context.getString(R.string.type_practical),
        context.getString(R.string.type_seminar),
        context.getString(R.string.type_training),
        context.getString(R.string.type_course_work),
        context.getString(R.string.type_independent_education)
    )
}
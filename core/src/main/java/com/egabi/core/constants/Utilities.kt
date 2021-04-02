package com.egabi.core.constants

import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

object Utilities {

    fun rxTextViewObservable(view: TextView): Observable<CharSequence>? {
        return RxTextView.textChanges(view).skip(1)
            .debounce(100, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

class EmailValidator {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.toRegex().matches(email)
        }
    }
}

class MobileNumberValidator {
    companion object {
        fun isMobileNumberValid(mobileNumber: String): Boolean {
            val uaeMobileRegex =
                "^(\\+971[\\s]{0,1}[\\-]{0,1}[\\s]{0,1}|[\\s]{0,1}0)(5[056]{1})[\\s]{0,1}[\\-]{0,1}[\\s]{0,1}[1-9]{1}[0-9]{6}\$"
            return uaeMobileRegex.toRegex().matches(mobileNumber)
        }
    }
}

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}
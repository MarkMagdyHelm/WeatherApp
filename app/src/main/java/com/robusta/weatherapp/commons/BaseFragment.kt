package com.robusta.weatherapp.commons

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.egabi.core.constants.ProgressDialogUtil
import com.egabi.core.constants.Utilities
import com.jakewharton.rxrelay2.BehaviorRelay
import com.robusta.weatherapp.R
import io.reactivex.disposables.Disposable

open class BaseFragment : Fragment() {
    private val progressdialog = CustomProgressDialog()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    fun bindEditText(
        edittext: EditText,
        behavior: BehaviorRelay<String?>
    ): Disposable? {
        return Utilities.rxTextViewObservable(edittext)!!
            .subscribe { t ->
                behavior.accept(
                    t.trim().toString()
                )
            }
    }
    fun showdialog()
    {
        progressdialog.show(requireContext(),
            requireActivity().getString(R.string.loading))
    }
    fun dismissdialog()
    {
        if (progressdialog!=null)
        {
            progressdialog.dialog?.dismiss()
        }

    }
    internal fun enableSubmitBtn(enable: Boolean, view: Button) {
        view.isEnabled = enable
        if (enable) {
            view.alpha = 1F
        } else {
            view.alpha = 0.4F
        }
    }
}
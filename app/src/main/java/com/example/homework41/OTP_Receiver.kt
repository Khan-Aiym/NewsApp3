package com.example.homework41

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Matcher
import java.util.regex.Pattern

class OTP_Receiver constructor() : BroadcastReceiver() {
    private var otpReceiverListener: OtpReceiverListener? = null
    fun initListener(otpReceiverListener: OtpReceiverListener?) {
        this.otpReceiverListener = otpReceiverListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if ((SmsRetriever.SMS_RETRIEVED_ACTION == intent.action)) {
            val bundle: Bundle? = intent.extras
            if (bundle != null) {
                val status: Status? = bundle.get(SmsRetriever.EXTRA_STATUS) as Status?
                if (status != null) {
                    when (status.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val message: String? =
                                bundle.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String?
                            if (message != null) {
                                val pattern: Pattern = Pattern.compile("\\d{6}")
                                val matcher: Matcher = pattern.matcher(message)
                                if (matcher.find()) {
                                    val myOtp: String = matcher.group(0)
                                    if (otpReceiverListener != null) {
                                        otpReceiverListener!!.onOtpSuccess(myOtp)
                                    } else {
                                        if (otpReceiverListener != null) {
                                            otpReceiverListener?.otpTimeOut()
                                        }
                                    }
                                }
                            }
                        }
                        CommonStatusCodes.TIMEOUT -> if (otpReceiverListener != null) {
                            otpReceiverListener!!.otpTimeOut()
                        }
                    }
                }
            }
        }
    }

    open interface OtpReceiverListener {
        fun onOtpSuccess(otp: String?)
        fun otpTimeOut()
    }
}
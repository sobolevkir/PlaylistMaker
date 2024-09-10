package com.sobolevkir.playlistmaker.settings.domain.impl

import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.ResourceProvider
import com.sobolevkir.playlistmaker.common.domain.ExternalNavigator
import com.sobolevkir.playlistmaker.settings.domain.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) : SharingInteractor {
    override fun shareApp() {
        val appUrl = resourceProvider.getString(R.string.url_share_app)
        externalNavigator.shareText(appUrl)
    }

    override fun openUserAgreement() {
        val userAgreementUrl = resourceProvider.getString(R.string.url_user_agreement)
        externalNavigator.openLink(userAgreementUrl)
    }

    override fun contactSupport() {
        val subject = resourceProvider.getString(R.string.support_mail_subject)
        val message = resourceProvider.getString(R.string.support_mail_message)
        val email = resourceProvider.getString(R.string.support_mail_address)
        externalNavigator.writeEmail(subject, message, email)
    }
}

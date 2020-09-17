package robin.sdk.keeplive

import androidx.annotation.NonNull
import java.io.Serializable

class ForegroundNotificationBean(private var title: String,
                                 private var description: String,
                                 private var iconRes: Int) : Serializable{
    private var foregroundNotificationClickListener: ForegroundNotificationClickListener? = null

    constructor(title: String,
                description: String,
                iconRes: Int,
                foregroundNotificationClickListener: ForegroundNotificationClickListener):this(title, description, iconRes) {
        this.foregroundNotificationClickListener = foregroundNotificationClickListener
    }

    /**
     * 设置标题
     * @param title 标题
     * @return ForegroundNotification
     */
    fun title(@NonNull title: String): ForegroundNotificationBean? {
        this.title = title
        return this
    }

    /**
     * 设置副标题
     * @param description 副标题
     * @return ForegroundNotification
     */
    fun description(@NonNull description: String): ForegroundNotificationBean? {
        this.description = description
        return this
    }

    /**
     * 设置图标
     * @param iconRes 图标
     * @return ForegroundNotification
     */
    fun icon(@NonNull iconRes: Int): ForegroundNotificationBean? {
        this.iconRes = iconRes
        return this
    }

    /**
     * 设置前台通知点击事件
     * @param foregroundNotificationClickListener 前台通知点击回调
     * @return ForegroundNotification
     */
    fun foregroundNotificationClickListener(@NonNull foregroundNotificationClickListener: ForegroundNotificationClickListener): ForegroundNotificationBean? {
        this.foregroundNotificationClickListener = foregroundNotificationClickListener
        return this
    }

    fun getTitle(): String {
        return if (title == null) "" else title
    }

    fun getDescription(): String {
        return if (description == null) "" else description
    }

    fun getIconRes(): Int {
        return iconRes
    }

    fun getForegroundNotificationClickListener(): ForegroundNotificationClickListener? {
        return foregroundNotificationClickListener
    }
}
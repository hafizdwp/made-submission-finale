package me.hafizdwp.made_submission_final.base

/**
 * @author hafizdwp
 * 10/07/19
 **/
data class BaseApiModel<T>(

        // success response
        var page: Int? = null,
        var total_results: Int? = null,
        var total_pages: Int? = null,
        var results: T? = null,

        // exclusive because too lazy too create another model
        var genres: T? = null,

        // error response
        var status_code: Int? = null,
        var status_message: String? = null,
        var success: Boolean? = null
)
package com.app.desafiodourado.core.firebase

class FirebaseConstants {
    object StatusCode {
        const val SUCCESS = 200
        const val NOT_FOUND = 404
    }

    object MethodsFirebaseClient {
        const val POST_DOCUMENT = "POST_DOCUMENT"
        const val GET_DOCUMENT_VALUE = "GET_DOCUMENT_VALUE"
        const val GET_SPECIFIC_DOCUMENT = "GET_SPECIFIC_DOCUMENT"
        const val GET_FILTER_DOCUMENT = "GET_FILTER_DOCUMENT"
        const val SET_SPECIFIC_DOCUMENT = "SET_SPECIFIC_DOCUMENT"
        const val UPDATE_DOCUMENT = "UPDATE_DOCUMENT"
        const val DELETE_DOCUMENT = "DELETE_DOCUMENT"
    }

    object MessageError {
        const val EMPTY_RESULT = "Lista vazia ou aparelho celular sem conexão."
    }

    object Collections {
        const val USERS = "users"
        const val CHALLENGERS = "challengers"
        const val MISSIONS = "missions"
    }
}
package com.app.desafiodourado.core.firebase

import com.google.firebase.firestore.DocumentSnapshot

interface FirebaseClient {
    suspend fun getDocumentValue(collectionPath: String): Result<List<DocumentSnapshot>>
    suspend fun <T> getDocumentWithOrderByValue(
        collectionPath: String,
        orderByName: String?,
    ): Result<T>

    suspend fun <T> getFilterDocuments(
        collectionPath: String,
        field: String,
        values: MutableList<String>,
    ): Result<T>

    suspend fun getSpecificDocument(
        collectionPath: String,
        documentPath: String
    ): Result<DocumentSnapshot>

    suspend fun <T> setSpecificDocument(
        collectionPath: String,
        documentPath: String,
        data: T,
    ): Result<Boolean>

    suspend fun updateDocument(
        collectionPath: String,
        documentPath: String,
        field: String,
        value: Any
    ): Result<Boolean>

    suspend fun createDocument(collectionPath: String, data: Any): Result<String>
    suspend fun deleteDocument(collectionPath: String, documentPath: String): Result<Boolean>
}
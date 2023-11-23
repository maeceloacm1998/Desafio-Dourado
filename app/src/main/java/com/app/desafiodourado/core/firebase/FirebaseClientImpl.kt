package com.app.desafiodourado.core.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.Exception

class FirebaseClientImpl(private val database: FirebaseFirestore) : FirebaseClient {
    override suspend fun getDocumentValue(collectionPath: String): Result<List<DocumentSnapshot>> {
        return try {
            val res = withContext(Dispatchers.IO) {
                database.collection(collectionPath).get().await()
            }
            val documents = res.documents

            if(documents.isEmpty()) {
                throw Exception()
            }

            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_DOCUMENT_VALUE,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = documents.toString()
            )
            Result.success(documents)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_DOCUMENT_VALUE,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = e.message.toString()
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    override suspend fun <T> getDocumentWithOrderByValue(
        collectionPath: String,
        orderByName: String?,
    ): Result<T> {
        return try {
            val res = withContext(Dispatchers.IO) {
                database.collection(collectionPath)
                    .orderBy(orderByName ?: "", Query.Direction.ASCENDING).get().await()
            }
            val documents = res.documents as T

            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_DOCUMENT_VALUE,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = documents.toString()
            )
            Result.success(documents)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_DOCUMENT_VALUE,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = FirebaseConstants.MessageError.EMPTY_RESULT
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    override suspend fun <T> getFilterDocuments(
        collectionPath: String,
        field: String,
        values: MutableList<String>,
    ): Result<T> {
        return try {
            val res = withContext(Dispatchers.IO) {
                database.collection(collectionPath).whereIn(field, values).get().await()
            }
            val documents = res.documents as T

            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_FILTER_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = documents.toString()
            )
            Result.success(documents)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_FILTER_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = FirebaseConstants.MessageError.EMPTY_RESULT
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    override suspend fun getSpecificDocument(
        collectionPath: String,
        documentPath: String
    ): Result<DocumentSnapshot> {
        return try {
            val res = withContext(Dispatchers.IO) {
                database.collection(collectionPath).document(documentPath).get().await()
            }

            val document = res as DocumentSnapshot

            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_SPECIFIC_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = document.toString()
            )
            Result.success(document)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.GET_SPECIFIC_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = FirebaseConstants.MessageError.EMPTY_RESULT
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    override suspend fun <T> setSpecificDocument(
        collectionPath: String,
        documentPath: String,
        data: T,
    ): Result<Boolean> {
        return try {
            withContext(Dispatchers.IO) {
                database.collection(collectionPath).document(documentPath).set(data as Any).await()
            }
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.SET_SPECIFIC_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = "SUCCESS SET DOCUMENT"
            )
            Result.success(true)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.SET_SPECIFIC_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = FirebaseConstants.MessageError.EMPTY_RESULT
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    override suspend fun createDocument(collectionPath: String, data: Any): Result<String> {
        return try {
            val id = withContext(Dispatchers.IO) {
                database.collection(collectionPath).document().id
            }
            withContext(Dispatchers.IO) {
                database.collection(collectionPath).document(id).set(data).await()
            }

            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.POST_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = "SUCCESS SET DOCUMENT"
            )
            Result.success(id)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.POST_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = FirebaseConstants.MessageError.EMPTY_RESULT
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    override suspend fun deleteDocument(
        collectionPath: String,
        documentPath: String
    ): Result<Boolean> {
        return try {
            withContext(Dispatchers.IO) {
                database.collection(collectionPath).document(documentPath).delete().await()
            }
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.DELETE_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = "SUCCESS DELETE DOCUMENT"
            )
            Result.success(true)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.DELETE_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = FirebaseConstants.MessageError.EMPTY_RESULT
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    override suspend fun updateDocument(
        collectionPath: String,
        documentPath: String,
        field: String,
        value: Any
    ) : Result<Boolean> {
        return try {
            withContext(Dispatchers.IO) {
                database.collection(collectionPath).document(documentPath).update(field, value).await()
            }
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.UPDATE_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.SUCCESS.toString(),
                data = "SUCCESS UPDATE DOCUMENT"
            )
            Result.success(true)
        } catch (e: Exception) {
            handleLog(
                typeRequisition = FirebaseConstants.MethodsFirebaseClient.UPDATE_DOCUMENT,
                collectionPath = collectionPath,
                statusCode = FirebaseConstants.StatusCode.NOT_FOUND.toString(),
                data = FirebaseConstants.MessageError.EMPTY_RESULT
            )
            Result.failure(Throwable(e.message, e.cause))
        }
    }

    private fun handleLog(
        typeRequisition: String,
        collectionPath: String,
        statusCode: String,
        data: String
    ) {
        Log.d(
            "=================> $typeRequisition",
            "PATH:$collectionPath -- STATUS_CODE:$statusCode -- DATA:$data"
        )
    }
}
package com.example.bookfilterapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface AuthorDAO {
    @Query("Select * from authordetails")
    suspend fun getAll():List<Authors>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(auth:Authors)
    @Query("Select * from authordetails where author=:name")
    suspend fun getAuthor(name:String):Authors
    @Query("Select * from authordetails a Join BooksDetail b on a.Aid=b.aid where Lower(a.author)=:name")
    suspend fun JoinedDetails(name:String?):List<AuthorsandBooks>
}
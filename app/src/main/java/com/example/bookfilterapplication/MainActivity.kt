package com.example.bookfilterapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authorInput = findViewById<TextInputLayout>(R.id.enteredAuthor)
        val titles = mutableListOf<bookdata>()

        val myApplication = application as BookFilterApp
        val authlist = myApplication.httpApiService
        val db = AppDatabase.getDatabase(this)

        val dataCount = findViewById<TextView>(R.id.firstResult)
        val dataResultTwo = findViewById<TextView>(R.id.secondResult)
        val dataResultThree = findViewById<TextView>(R.id.ThirdResult)
        val dataResultFour = findViewById<TextView>(R.id.fourthResult)
        val filterButton = findViewById<Button>(R.id.button)
        filterButton.setOnClickListener {
            titles.clear()
            dataCount.text = ""
            dataResultTwo.text = ""
            var insertCount: Int
            dataResultTwo.text = ""
            CoroutineScope(Dispatchers.IO).launch {

                var result = authlist.getMyBookData()
                for (i in result)
                    titles.add(i)
            }
            GlobalScope.launch {
                var auth: Int = 0
                for (item in titles) {
                    titles.add(item)
                    insertCount = 0
                    val AuthorsList: List<Authors> = db.authorDao().getAll()
                    //var aa: AuthorDetails = AuthorDetails(author = item.author, country = item.country)
                    for (items in AuthorsList) {
                        if (items.author.lowercase() == item.author.lowercase()) {
                            auth = items.Aid
                            insertCount = 1
                            break
                        }
                    }
                    if (insertCount == 1) {
                        db.BookDao()
                            .InsertBooks(
                                Book(
                                    aid = auth,
                                    language = item.language,
                                    imageLink = item.imageLink,
                                    link = item.link,
                                    pages = item.pages,
                                    title = item.title,
                                    year = item.year
                                )
                            )
                    } else {
                        db.authorDao().insert(
                            Authors(
                                author = item.author,
                                country = item.country
                            )
                        )

                        auth = db.authorDao()
                            .getAuthor(item.author).Aid
                        db.BookDao()
                            .InsertBooks(
                                Book(
                                    aid = auth,
                                    language = item.language,
                                    imageLink = item.imageLink,
                                    link = item.link,
                                    pages = item.pages,
                                    title = item.title,
                                    year = item.year
                                )
                            )
                    }
                }
                val list: List<AuthorsandBooks> = db.authorDao()
                    .JoinedDetails(authorInput.editText?.text?.toString()?.lowercase())
                withContext(Dispatchers.Main) {

                    var count: Int = 0
                    var res = ""

                    if (list.size >= 1) {
                        res += "Result: ${list[0].title} (${list[0].BookID})\n"
                        count += 1
                    }
                    if (list.size >= 2) {
                        res += "Result: ${list[1].title} (${list[1].BookID})\n"
                        count += 1
                    }
                    if (list.size >= 3) {
                        res += "Result: ${list[2].title} (${list[2].BookID})\n"
                        count += 1
                    }
                    dataCount.text = "Result: $count"
                    dataResultTwo.text = res
                }
            }
        }

    }
}

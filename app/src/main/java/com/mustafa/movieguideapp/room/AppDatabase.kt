package com.mustafa.movieguideapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mustafa.movieguideapp.models.entity.*
import com.mustafa.movieguideapp.utils.IntegerListConverter
import com.mustafa.movieguideapp.utils.KeywordListConverter
import com.mustafa.movieguideapp.utils.ReviewListConverter
import com.mustafa.movieguideapp.utils.StringListConverter
import com.mustafa.movieguideapp.utils.VideoListConverter

@Database(
    entities = [(Movie::class),
        (Tv::class),
        (Person::class),
        (SearchMovieResult::class),
        (DiscoveryMovieResult::class),
        (DiscoveryTvResult::class),
        (MovieRecentQueries::class),
        (SearchTvResult::class),
        (TvRecentQueries::class),
        (MovieSuggestionsFts::class),
        (TvSuggestionsFts::class),
        (FilteredMovieResult::class),
        (FilteredTvResult::class),
        (PeopleResult::class),
        (MoviePerson::class),
        (TvPerson::class),
        (MoviePersonResult::class),
        (TvPersonResult::class),
        (SearchPeopleResult::class),
        (PeopleRecentQueries::class),
        (PeopleSuggestionsFts::class)],
    version = 32, exportSchema = false
)
@TypeConverters(
    value = [(StringListConverter::class), (IntegerListConverter::class),
        (KeywordListConverter::class), (VideoListConverter::class), (ReviewListConverter::class)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao
    abstract fun peopleDao(): PeopleDao
}

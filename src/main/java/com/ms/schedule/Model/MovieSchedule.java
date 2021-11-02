package com.ms.schedule.Model;

import com.ms.movie.Movie;
import com.ms.schedule.ConfigurationModel.MovieAvailablePeriod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MovieSchedule {
    private Movie movie;
    private MovieAvailablePeriod period;
}

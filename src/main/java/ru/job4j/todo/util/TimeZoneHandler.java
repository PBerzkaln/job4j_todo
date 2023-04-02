package ru.job4j.todo.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.ZoneId;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeZoneHandler {

    public static void setZone(User user, Task task) {
        var defaultZone = TimeZone.getDefault().toZoneId();
        var userZone = ZoneId.of(user.getTimezone());
        var time = task.getCreated()
                .atZone(defaultZone)
                .withZoneSameInstant(userZone)
                .toLocalDateTime();
        task.setCreated(time);
    }
}
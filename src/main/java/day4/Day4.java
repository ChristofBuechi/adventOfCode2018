package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static day4.Day4.Activity.BeginsShift;
import static java.util.stream.Collectors.groupingBy;

public class Day4 {


    private final List<GuardActivity> guardActivityList;

    public static void main(String[] args) throws IOException {
        Day4 day4 = new Day4("src/main/resources/day4.txt");
        day4.printStrategy1Result();

    }

    private void printStrategy1Result() {
        int mostSleepingGuard = findMostSleptGuard();
        Map<LocalDate, List<GuardActivity>> trackingActivities = guardActivityList
                .stream()
                .filter(guardActivity -> guardActivity.guardNumber == mostSleepingGuard && guardActivity.activity != BeginsShift)
                .collect(groupingBy(guardActivity -> guardActivity.dateTime.toLocalDate()));

        Map<LocalDate, int[]> list = new HashMap<>(trackingActivities.size());
        trackingActivities.forEach(new BiConsumer<LocalDate, List<GuardActivity>>() {
            @Override
            public void accept(LocalDate localDate, List<GuardActivity> guardActivities) {
                int[] minutes = new int[60];
                boolean isSleeping = false;
                for (int actualMinute = 0; actualMinute < minutes.length; actualMinute++) {

                    for (GuardActivity guardActivity : guardActivities) {
                        if (guardActivity.dateTime.getMinute() == actualMinute) {
                            switch (guardActivity.activity) {
                                case FallsAsleep:
                                    isSleeping = true;
                                    break;
                                case WakesUp:
                                    isSleeping = false;
                                    break;
                            }
                        }
                    }

                    if (isSleeping) {
                        minutes[actualMinute] = 1;
                    }
                }
                list.put(localDate, minutes);
            }
        });

        int[] sleepingCountPerMinute = new int[60];
        for (int minute = 0; minute < sleepingCountPerMinute.length; minute++) {
            int count = 0;

            for (int[] value : list.values()) {
                if (value[minute] == 1) {
                    count++;
                }
            }
            sleepingCountPerMinute[minute] = count;
        }

        int mostSleepingMinute = -1;
        for (int minute = 0; minute < sleepingCountPerMinute.length; minute++) {
            if (mostSleepingMinute == -1 || sleepingCountPerMinute[minute] > sleepingCountPerMinute[mostSleepingMinute]) {
                mostSleepingMinute = minute;
            }
        }


        System.out.println("Actual Result = " + mostSleepingGuard * mostSleepingMinute);


    }

    private Integer findMostSleptGuard() {
        Map<Integer, Integer> guardAndSleepMinutes = new HashMap<>();
        Integer guardNumber = -1;
        Integer startSleepTime = -1;
        for (GuardActivity guardActivity : guardActivityList) {
            switch (guardActivity.activity) {
                case BeginsShift:
                    guardNumber = guardActivity.guardNumber;
                    break;
                case FallsAsleep:
                    startSleepTime = guardActivity.dateTime.getMinute();
                    break;
                case WakesUp:
                    int endSleep = guardActivity.dateTime.getMinute();
//                    if (startSleepTime > endSleep) {
//                        endSleep += 60;
//                    }
                    int sleepTime = endSleep - startSleepTime;
                    startSleepTime = 0;
                    int alreadySleptMinutes = 0;
                    if (guardAndSleepMinutes.containsKey(guardNumber)) {
                        alreadySleptMinutes = guardAndSleepMinutes.get(guardNumber);
                    }
                    guardAndSleepMinutes.put(guardNumber, alreadySleptMinutes + sleepTime);
            }

        }
        Optional<Map.Entry<Integer, Integer>> guard = guardAndSleepMinutes.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue));

        if (guard.isPresent()) {
            System.out.println("Guard Number: " + guard.get().getKey() + " has sleept for total minutes: " + guard.get().getValue());
            return guard.get().getKey();
        }

        return 0;
    }

    public class ActiveGuard {
        public Integer value;
    }

    public Day4(String filePath) throws IOException {
        ActiveGuard activeGuard = new ActiveGuard();
        guardActivityList = Files.lines(Paths.get(filePath)).map(GuardActivity::new).sorted(Comparator.comparing(o -> o.dateTime)).map(guardActivity -> {
            if (guardActivity.guardNumber == null) {
                guardActivity.withGuardNumber(activeGuard.value);
            } else {
                activeGuard.value = guardActivity.guardNumber;
            }
            return guardActivity;
        }).collect(Collectors.toList());
    }

    private class GuardActivity {
        public final LocalDateTime dateTime;
        public Integer guardNumber;
        public final Activity activity;


        public GuardActivity(String entry) {
            int endTime = entry.indexOf("]");
            String dateString = entry.substring(entry.indexOf("[") + 1, endTime);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateTime = LocalDateTime.parse(dateString, formatter);

            String action = entry.substring(endTime + 1).trim();
            if (action.startsWith("Guard")) {
                guardNumber = Integer.parseInt(action.split(" ")[1].substring(1));
                activity = BeginsShift;
            } else if (action.startsWith("falls asleep")) {
                guardNumber = null;
                activity = Activity.FallsAsleep;
            } else {
                guardNumber = null;
                activity = Activity.WakesUp;
            }
        }

        public GuardActivity withGuardNumber(Integer value) {
            this.guardNumber = value;
            return this;
        }
    }

    public enum Activity {
        FallsAsleep, WakesUp, BeginsShift
    }
}

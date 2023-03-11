package com.studyexchange.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Matcher {
    private final Map<Filter, FilteredHelpRequests> queue;

    public Matcher() {
        this.queue = new HashMap<>();
    }

    public Optional<HelpRequest> matchHelpRequestOrEnqueue(User user, HelpRequest helpRequest) {
        for (Subject canHelp : user.getSubjectsCanHelpWith()) {
            Optional<HelpRequest> opposite = dequeueOpposite(canHelp, user, helpRequest);
            if (opposite.isPresent()) {
                return opposite;
            }
        }
        for (Subject canHelp : user.getSubjectsCanHelpWith()) {
            enqueue(canHelp, user.getGrade(), helpRequest);
        }
        return Optional.empty();
    }

    @SuppressWarnings("WhileLoopReplaceableByForEach")
    private Optional<HelpRequest> dequeueOpposite(Subject canHelp, User user, HelpRequest helpRequest) {
        Filter oppositeFilter = new Filter(helpRequest.getSubject(), canHelp, user.getGrade());
        FilteredHelpRequests filteredHelpRequests = queue.get(oppositeFilter);
        Set<HelpRequest> oppositeHelpRequests = filteredHelpRequests.getHelpRequests();
        Iterator<HelpRequest> iterator = oppositeHelpRequests.iterator();
        while (iterator.hasNext()) {
            HelpRequest oppositeHelpRequest = iterator.next();
            if (!user.isRequestSeen(helpRequest)) {
                filteredHelpRequests.removeHelpRequest(oppositeHelpRequest);
                return Optional.of(oppositeHelpRequest);
            }
        }
        return Optional.empty();
    }

    private void enqueue(Subject canHelp, Grade grade, HelpRequest helpRequest) {
        Filter filter = new Filter(canHelp, helpRequest.getSubject(), grade);
        if (queue.containsKey(filter)) {
            FilteredHelpRequests helpRequests = queue.get(filter);
            helpRequests.addHelpRequest(helpRequest);
        } else {
            FilteredHelpRequests filteredHelpRequests = new FilteredHelpRequests();
            filteredHelpRequests.addHelpRequest(helpRequest);
            queue.put(filter, filteredHelpRequests);
        }
    }

    private record Filter(Subject canHelp, Subject needsHelp, Grade grade) {
        @Override
        public int hashCode() {
            return Objects.hash(canHelp, needsHelp, grade);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Filter filter = (Filter) o;
            return canHelp == filter.canHelp && needsHelp == filter.needsHelp && grade == filter.grade;
        }
    }

    private static class FilteredHelpRequests {
        private final Set<HelpRequest> helpRequests;

        FilteredHelpRequests() {
            this.helpRequests = new LinkedHashSet<>();
        }

        public void addHelpRequest(HelpRequest helpRequest) {
            helpRequests.add(helpRequest);
        }

        public Set<HelpRequest> getHelpRequests() {
            return helpRequests;
        }

        public void removeHelpRequest(HelpRequest helpRequest) {
            helpRequests.remove(helpRequest);
        }
    }

}

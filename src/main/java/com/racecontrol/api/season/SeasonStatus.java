package com.racecontrol.api.season;

import com.racecontrol.api.core.exception.BusinessRuleException;

public enum SeasonStatus {
    REGISTRATION_OPEN {
        @Override
        public void validateTransition(SeasonStatus nextStatus) {
            if (nextStatus != IN_PROGRESS) {
                throw new BusinessRuleException("Cannot transition from REGISTRATION_OPEN to " + nextStatus);
            }
        }
    },

    IN_PROGRESS {
        @Override
        public void validateTransition(SeasonStatus nextStatus) {
            if (nextStatus != FINISHED) {
                throw new BusinessRuleException("Cannot transition from IN_PROGRESS to " + nextStatus);
            }
        }
    },

    FINISHED {
        @Override
        public void validateTransition(SeasonStatus nextStatus) {
            throw new BusinessRuleException("Cannot transition from FINISHED to " + nextStatus);
        }
    };

    public abstract void validateTransition(SeasonStatus nextStatus);
}

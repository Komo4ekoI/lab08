package it.unibo.deathnote;

import static java.lang.System.currentTimeMillis;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;
/**
 * DeathNoteImpl implements the interface {@link DeathNote}.
 */

public class DeathNoteImpl implements DeathNote {
    private final Map<String, DeathNoteRow> deaths = new LinkedHashMap<>();
    private String lastWrittenName;

    /**
     * New death note.
     */
    public DeathNoteImpl() {
        // Does not require the initialization
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Rule #" + ruleNumber + "does not exist");
        }
        return RULES.get(ruleNumber - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name);
        lastWrittenName = name;
        deaths.put(name, new DeathNoteRow());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(final String cause) {
        validateNameAndField(lastWrittenName, cause, "cause");
        final var oldDeathRow = deaths.get(lastWrittenName);
        final var newDeathRow = oldDeathRow.upsertDeathCause(cause);
        if (oldDeathRow.equals(newDeathRow)) {
            return false;
        } else {
            deaths.put(lastWrittenName, newDeathRow);
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(final String details) {
        validateNameAndField(lastWrittenName, details, "details");
        final var oldDeathRow = deaths.get(lastWrittenName);
        final var newDeathRow = oldDeathRow.upsertDeathDetails(details);
        if (oldDeathRow.equals(newDeathRow)) {
            return false;
        } else {
            deaths.put(lastWrittenName, newDeathRow);
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(final String name) {
        return findDeathRow(name).cause;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(final String name) {
        return findDeathRow(name).details;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(final String name) {
        return deaths.containsKey(name);
    }

    private DeathNoteRow findDeathRow(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        final var death = deaths.get(name);
        if (death == null) {
            throw new IllegalArgumentException("Name :" + name + " is not found");
        }
        return death;
    }

    private void validateNameAndField(final String name, final String field, final String filedName) {
        if (name == null) {
            throw new IllegalStateException("There is no name written into Death Note");
        }
        if (field == null) {
            throw new IllegalStateException("A " + filedName + " can't be null");
        }
    }

    private static final class DeathNoteRow {
        private static final String DEFAULT_CAUSE = "heart attack";
        private static final int MAX_MILLS_TO_SPECIFY_CAUSE_OF_DEATH = 40;
        private static final int MAX_MILLS_TO_SPECIFY_DETAILS_OF_DEATH = 6040;
        private final String cause;
        private final String details;
        private final long nameWrittenAtMills;

        private DeathNoteRow(
            final String deathCause, 
            final String deathDetails, 
            final long nameWrittenAtMills) {
            cause = deathCause;
            details = deathDetails;
            this.nameWrittenAtMills = nameWrittenAtMills;
        }

        DeathNoteRow() {
            this(DEFAULT_CAUSE, "", currentTimeMillis());
        }

        DeathNoteRow upsertDeathCause(final String newCause) {
            if (currentTimeMillis() - nameWrittenAtMills 
            > MAX_MILLS_TO_SPECIFY_CAUSE_OF_DEATH) {
                return this;
            }
            return new DeathNoteRow(newCause, details, nameWrittenAtMills);
        }

        DeathNoteRow upsertDeathDetails(final String newDetails) {
            if (currentTimeMillis() - nameWrittenAtMills 
            > MAX_MILLS_TO_SPECIFY_DETAILS_OF_DEATH) {
                return this;
            }
            return new DeathNoteRow(cause, newDetails, nameWrittenAtMills);
        }

        @Override 
        public int hashCode() {
            return Objects.hash(cause, details, nameWrittenAtMills);
        }

        @Override 
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof DeathNoteRow other)) {
                return false;
            }
            return Objects.equals(other.cause, cause)
            && Objects.equals(other.details, details)
            && Objects.equals(other.nameWrittenAtMills, nameWrittenAtMills);
        }
    }
}

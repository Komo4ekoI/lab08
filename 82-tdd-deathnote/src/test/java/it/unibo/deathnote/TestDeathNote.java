package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import it.unibo.deathnote.api.DeathNote;

class TestDeathNote {
    private static final int MIN_ERROR_LENGTH = 10;
    private static final String TEST_NAME = "Vadym Teliatnyk";
    private static final String SECOND_TEST_NAME = "Makoto Yuki";
    private static final String HEART_ATTACK = "heart attack";
    private static final String KARTING_ACCIDENT = "karting accident";
    private static final String TEST_ACCIDENT = "Java Misstype";
    private static final long SLEEP_TIME = 100L;
    private static final String TEST_DEATH_DETAILS = "ran for too long";
    private static final long TEST_DEATH_DETAILS_SLEEP_TIME = 6100L;

    private DeathNote deathNote;

    @BeforeEach 
    void prepareTest() {
        deathNote = new DeathNoteImpl();
    }

    @Test
    void noZeroAndNegativeIndexRules() {
        final var error = assertThrows(
            IllegalArgumentException.class, 
            new Executable() {
            @Override
            public void execute() throws Throwable {
                deathNote.getRule(0);
                deathNote.getRule(-1);
            }
        });
        assertNotNull(error.getMessage());
        assertTrue(error.getMessage().length() > MIN_ERROR_LENGTH);
    }

    @Test
    void noRuleIsEmptyOrNull() {
        for (var index = 1; index <= DeathNote.RULES.size(); index++) {
            final var rule = deathNote.getRule(index);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    @Test
    void humanDie() {
        assertFalse(deathNote.isNameWritten(TEST_NAME));
        deathNote.writeName(TEST_NAME);
        assertTrue(deathNote.isNameWritten(TEST_NAME));
        assertFalse(deathNote.isNameWritten(SECOND_TEST_NAME));
        assertFalse(deathNote.isNameWritten(""));
    }

    @Test
    void assertDeathCause() {
        final var error = assertThrows(
            IllegalArgumentException.class, 
            new Executable() {
            @Override
            public void execute() throws Throwable {
                deathNote.getDeathCause(TEST_NAME);
            }
        });
        assertNotNull(error.getMessage());
        assertTrue(error.getMessage().length() > MIN_ERROR_LENGTH);
        deathNote.writeName(TEST_NAME);
        assertEquals(HEART_ATTACK, deathNote.getDeathCause(TEST_NAME));
        deathNote.writeName(SECOND_TEST_NAME);
        assertTrue(deathNote.writeDeathCause(KARTING_ACCIDENT));
        assertEquals(KARTING_ACCIDENT, deathNote.getDeathCause(SECOND_TEST_NAME));
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (final InterruptedException e) {
            fail(e);
        }
        assertFalse(deathNote.writeDeathCause(TEST_ACCIDENT));
        assertEquals(KARTING_ACCIDENT, deathNote.getDeathCause(SECOND_TEST_NAME));
    }

    @Test
    void assertDeathDetails() {
        final var error = assertThrows(
            IllegalStateException.class, 
            new Executable() {
            @Override 
            public void execute() throws Throwable {
                deathNote.writeDetails(TEST_DEATH_DETAILS);
            }
        });
        assertNotNull(error.getMessage());
        assertTrue(error.getMessage().length() > MIN_ERROR_LENGTH);
        deathNote.writeName(TEST_NAME);
        assertTrue(deathNote.getDeathDetails(TEST_NAME).isEmpty());
        assertTrue(deathNote.writeDetails(TEST_DEATH_DETAILS));
        assertEquals(TEST_DEATH_DETAILS, deathNote.getDeathDetails(TEST_NAME));
        deathNote.writeName(SECOND_TEST_NAME);
        try {
            Thread.sleep(TEST_DEATH_DETAILS_SLEEP_TIME);
        } catch (final InterruptedException e) {
            fail(e);
        }
        assertFalse(deathNote.writeDetails(TEST_DEATH_DETAILS));
        assertTrue(deathNote.getDeathDetails(SECOND_TEST_NAME).isEmpty());
    }
}

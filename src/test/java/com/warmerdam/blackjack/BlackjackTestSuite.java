package com.warmerdam.blackjack;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.warmerdam.blackjack.test.HandsTest;
import com.warmerdam.blackjack.test.PlayerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({HandsTest.class, PlayerTest.class})
public class BlackjackTestSuite {
}

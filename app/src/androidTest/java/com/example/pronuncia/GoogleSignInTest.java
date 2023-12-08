package com.example.pronuncia;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.espresso.assertion.ViewAssertions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GoogleSignInTest {

    @Rule
    public ActivityScenarioRule<RegisterPage> activityScenarioRule =
            new ActivityScenarioRule<>(RegisterPage.class);

    @Test
    public void loginWithGoogle() {
        // Simular inicio de sesión con Google
        // Aquí deberías realizar la acción de iniciar sesión con Google
        // utilizando Firebase Authentication

        // Luego, verifica si la acción fue exitosa
        // Por ejemplo, podrías verificar si un elemento específico aparece
        // después de iniciar sesión con éxito, supongamos un TextView en la pantalla principal

        Espresso.onView(ViewMatchers.withId(R.id.textViewWelcomeMessage))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}

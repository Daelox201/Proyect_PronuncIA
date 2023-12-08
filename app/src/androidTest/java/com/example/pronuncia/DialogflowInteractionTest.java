package com.example.pronuncia;

import androidx.test.espresso.Espresso;
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
public class DialogflowInteractionTest {

    @Rule
    public ActivityScenarioRule<RegisterPage> activityScenarioRule =
            new ActivityScenarioRule<>(RegisterPage.class);

    @Test
    public void dialogflowInteractionTest() {
        // Simular la interacción con Dialogflow
        // Aquí puedes simular la interacción de tu aplicación con Dialogflow,
        // por ejemplo, utilizando las respuestas de Dialogflow para comprobar
        // si los elementos esperados en tu interfaz de usuario aparecen después de esa interacción.

        // Por ejemplo, verifica si un elemento específico aparece
        // después de la interacción con Dialogflow.
        // Supongamos que esperas que aparezca un TextView con el ID textViewResponse.

        Espresso.onView(ViewMatchers.withId(R.id.textViewResponse))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}

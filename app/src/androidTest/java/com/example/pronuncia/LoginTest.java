package com.example.pronuncia;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.RootMatchers;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    @Rule
    public ActivityScenarioRule<RegisterPage> activityScenarioRule =
            new ActivityScenarioRule<>(RegisterPage.class);

    @Test
    public void loginWithValidCredentials() {
        // Simular inicio de sesión con credenciales válidas
        Espresso.onView(ViewMatchers.withId(R.id.txtEmail))
                .perform(ViewActions.typeText("correo@upp251.com"));

        Espresso.onView(ViewMatchers.withId(R.id.psw))
                .perform(ViewActions.replaceText("ContSeg7129$"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.btnSing_In)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.btnSing_In))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void loginWithInvalidEmail() {
        // Simular inicio de sesión con correo electrónico inválido
        Espresso.onView(ViewMatchers.withId(R.id.txtEmail))
                .perform(ViewActions.replaceText("correo_invalido"), ViewActions.closeSoftKeyboard());

        // Intentar avanzar o ejecutar la acción que dispara la validación
        Espresso.onView(ViewMatchers.withId(R.id.btnSing_In)).perform(ViewActions.click());

        // Verificar que se muestre el mensaje de error en el campo de correo electrónico
        Espresso.onView(ViewMatchers.withId(R.id.txtEmail))
                .check(ViewAssertions.matches(
                        ViewMatchers.hasErrorText("Correo electrónico no válido")));
    }



}

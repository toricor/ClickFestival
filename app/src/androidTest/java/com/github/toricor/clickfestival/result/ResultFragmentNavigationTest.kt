package com.github.toricor.clickfestival.result

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.toricor.clickfestival.R
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat

@RunWith(AndroidJUnit4::class)
class ResultFragmentNavigationTest {

    @Test
    fun testNavigationToTopFragment() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        // Create a graphical FragmentScenario for the ResultFragment
        val resultScenario = launchFragmentInContainer {
            ResultFragment().also { fragment ->
                // In addition to returning a new instance of our Fragment,
                // get a callback whenever the fragment’s view is created
                // or destroyed so that we can set the NavController
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment’s view has just been created
                        navController.setGraph(R.navigation.nav_graph)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                        navController.setCurrentDestination(R.id.ResultFragment)
                    }
                }
            }
        }

        // Verify that performing a click changes the NavController's state
        onView(ViewMatchers.withId(R.id.back_home)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.TopFragment)
    }

    @Test
    fun testNavigationToGameFragment() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        ).apply {
            // Set the graph on the TestNavHostController
            setGraph(R.navigation.nav_graph)
            setCurrentDestination(R.id.ResultFragment)
        }

        // Create a graphical FragmentScenario for the ResultFragment
        val resultScenario = launchFragmentInContainer<ResultFragment>()

        resultScenario.onFragment { fragment ->
            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController's state
        onView(ViewMatchers.withId(R.id.play_again)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.GameFragment)
    }
}
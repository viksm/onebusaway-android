package org.onebusaway.android.util;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import org.onebusaway.android.R;
import org.onebusaway.android.app.Application;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * A class containing utility methods related to showing a tutorial to users for how to use various
 * OBA features, using the ShowcaseView library (https://github.com/amlcurran/ShowcaseView).
 */
public class ShowcaseViewUtils {

    public static final String TUTORIAL_WELCOME = ".tutorial_welcome";

    /**
     * Shows the tutorial for the specified
     *
     * @param tutorialType type of tutorial to show, defined by the TUTORIAL_* constants in
     *                     ShowcaseViewUtils
     * @param activity     activity used to show the tutorial
     */
    public synchronized static void showTutorial(String tutorialType, AppCompatActivity activity) {
        Resources r = activity.getResources();
        SharedPreferences settings = Application.getPrefs();
        boolean showTutorial = settings.getBoolean(tutorialType, true);

        String title;
        String text;
        Target target;

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

        switch (tutorialType) {
            case TUTORIAL_WELCOME:
                title = r.getString(R.string.tutorial_welcome_title);
                text = r.getString(R.string.tutorial_welcome_text);
                target = new ShowcaseViewUtils().new ToolbarActionItemTarget(toolbar,
                        R.id.action_search);
            default:
                // Default to welcome message - this shouldn't happen
                title = r.getString(R.string.tutorial_welcome_title);
                text = r.getString(R.string.tutorial_welcome_text);
                target = new ShowcaseViewUtils().new ToolbarActionItemTarget(toolbar,
                        R.id.action_search);
                //new ViewTarget(R.id.btnMyLocation, activity)
        }

        if (showTutorial) {
            //PreferenceUtils.saveBoolean(tutorialType, false);
            new ShowcaseView.Builder(activity)
                    .setTarget(target)
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentTitle(title)
                    .setContentText(text)
//                    .hideOnTouchOutside()
                    .build();
        }
    }

    /**
     * Resets all tutorials so they are shown to the user again
     */
    public static void resetAllTutorials() {
        PreferenceUtils.saveBoolean(TUTORIAL_WELCOME, true);
    }

    /**
     * ToolbarActionItemTarget is derived from https://github.com/amlcurran/ShowcaseView sample app
     *
     * Copyright 2014 Alex Curran
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     *
     * Represents an Action item to showcase (e.g., one of the buttons on an ActionBar).
     * To showcase specific action views such as the home button, use this.
     */
    private class ToolbarActionItemTarget implements Target {

        private final Toolbar toolbar;

        private final int menuItemId;

        public ToolbarActionItemTarget(Toolbar toolbar, @IdRes int itemId) {
            this.toolbar = toolbar;
            this.menuItemId = itemId;
        }

        @Override
        public Point getPoint() {
            return new ViewTarget(toolbar.findViewById(menuItemId)).getPoint();
        }

    }
}

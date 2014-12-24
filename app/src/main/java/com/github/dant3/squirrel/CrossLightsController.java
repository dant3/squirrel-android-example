/**
 * Copyright 2014 Vyacheslav Blinov (blinov.vyacheslav@gmail.com)
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
 */

package com.github.dant3.squirrel;

import android.os.Handler;
import com.github.dant3.squirrel.utils.Observable;
import com.github.dant3.squirrel.utils.ObservableSupport;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.squirrelframework.foundation.fsm.AnonymousAction;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

@Slf4j
public class CrossLightsController extends
        AbstractStateMachine<CrossLightsController, Light, CrossLightsController.Event, Object>
        implements Observable<CrossLightsController> {
    public static final int SWITCH_TIME_MILLIS = 1000;

    @Delegate
    private final ObservableSupport<CrossLightsController> observableSupport =
            new ObservableSupport<CrossLightsController>(this);

    public static CrossLightsController create() {
        StateMachineBuilder<CrossLightsController, Light, Event, Object> builder =
                StateMachineBuilderFactory.create(CrossLightsController.class, Light.class, Event.class, Object.class);

        builder.externalTransition().from(Light.RED).to(Light.YELLOW).on(Event.TurnYellow).perform(new FireEventAfter(Event.TurnGreen, SWITCH_TIME_MILLIS));
        builder.externalTransition().from(Light.GREEN).to(Light.YELLOW).on(Event.TurnYellow).perform(new FireEventAfter(Event.TurnRed, SWITCH_TIME_MILLIS));
        builder.externalTransition().from(Light.YELLOW).to(Light.GREEN).on(Event.TurnGreen);
        builder.externalTransition().from(Light.YELLOW).to(Light.RED).on(Event.TurnRed);

        builder.onEntry(Light.RED).perform(new FireEventAfter(Event.TurnYellow, SWITCH_TIME_MILLIS));
        builder.onEntry(Light.GREEN).perform(new FireEventAfter(Event.TurnYellow, SWITCH_TIME_MILLIS));

        return builder.newStateMachine(Light.RED);
    }

    @RequiredArgsConstructor(suppressConstructorProperties = true)
    private static class FireEventAfter extends AnonymousAction<CrossLightsController, Light, Event, Object> {
        private final Event eventToFire;
        private final int delay;
        private final Handler handler=new Handler();

        @Override
        public void execute(Light from, final Light to, final Event event, Object context, final CrossLightsController stateMachine) {
            Runnable action = new Runnable() {
                public void run() {
                    stateMachine.notifyObservers();
                    log.warn("{} lighted", to.toString());
                    stateMachine.fire(eventToFire);
                }
            };
            handler.postDelayed(action, delay);
        }
    }


    public static enum Event {
        TurnRed,
        TurnYellow,
        TurnGreen
    }
}

package com.github.dant3.squirrel;


import lombok.RequiredArgsConstructor;
import org.squirrelframework.foundation.fsm.AnonymousAction;
import org.squirrelframework.foundation.fsm.StateMachineBuilder;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

public class CrossLightsController extends
        AbstractStateMachine<CrossLightsController, Light, CrossLightsController.Event, Object> {

    public static CrossLightsController create() {
        StateMachineBuilder<CrossLightsController, Light, Event, Object> builder =
                StateMachineBuilderFactory.create(CrossLightsController.class, Light.class, Event.class, Object.class);

        builder.externalTransition().from(Light.RED).to(Light.YELLOW).on(Event.TurnYellow);
        builder.externalTransition().from(Light.GREEN).to(Light.YELLOW).on(Event.TurnYellow);
                builder.externalTransition().from(Light.YELLOW).to(Light.GREEN).on(Event.TurnGreen);
        builder.externalTransition().from(Light.YELLOW).to(Light.RED).on(Event.TurnRed);

        return builder.newStateMachine(Light.RED);
    }


    @RequiredArgsConstructor(suppressConstructorProperties = true)
    private static class FireEventAfter extends AnonymousAction<CrossLightsController, Light, Event, Object> {
        private final Event eventToFire;
        private final int delay;

        @Override
        public void execute(Light from, Light to, Event event, Object context, CrossLightsController stateMachine) {
            // TODO
        }
    }


    public static enum Event {
        TurnRed,
        TurnYellow,
        TurnGreen
    }
}

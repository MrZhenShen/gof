package practice.design.structural.facade.solution;

import practice.design.structural.facade.solution.service.VideoPublider;

public class Main {
    public static void main(String[] args) {

        System.out.println(new VideoPublider().publish("input/source.mov"));
    }
}

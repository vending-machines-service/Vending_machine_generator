package vms.vmsmachineemulator.util;

import java.util.Random;

public class UtilService {

  public static int getRandomNumber(int from, int to) {
    return new Random().ints(1, from, to).findFirst().getAsInt();
  }
}
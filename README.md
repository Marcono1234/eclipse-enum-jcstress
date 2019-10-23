# eclipse-enum-jcstress
Maven project using jcstress to test whether the lazy initialization code created by Eclipse's ecj for switches on enums is thread-safe.

# Background
When using a `switch` statement on enum values, Java compilers introduce an additional level of indirection instead of directly switching on the `ordinal()` value, see https://www.benf.org/other/cfr/switch-on-enum.html.  
`javac` introduces a nested class which, when loaded, initializes an array mapping from ordinal values to a 1-based index.   
Eclipse's ecj uses a different approach. It lazily initializes an array the first time the index for an enum value is looked up:
```
package mypackage;

public enum MyEnum {
    FIRST;

    public int get() {
        switch (this) {
            case FIRST:
                return 2;
            default:
                return -1;
        }
    }
}
```
Decompiled:
```
package mypackage;

public enum MyEnum {
   FIRST;

   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$mypackage$MyEnum;

   public int get() {
      switch($SWITCH_TABLE$mypackage$MyEnum()[this.ordinal()]) {
      case 1:
         return 2;
      default:
         return -1;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$mypackage$MyEnum() {
      int[] var10000 = $SWITCH_TABLE$mypackage$MyEnum;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[values().length];

         try {
            var0[FIRST.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$mypackage$MyEnum = var0;
         return var0;
      }
   }
}
```
However, since there is no synchronization, this lead to the question: [Is Eclipse's $SWITCH_TABLE$ thread-safe?](https://stackoverflow.com/q/58507978/4288506)

This project uses [jcstress](https://wiki.openjdk.java.net/display/CodeTools/jcstress) to find this out. The class [EclipseEnumTest](src/main/java/test/EclipseEnumTest.java) emulates the code which would be created by ecj.


# Run
The following compiles the sources and also runs jcstress (if `java` is in the `PATH` environment variable).
```
mvn clean verify
```

# Result
Unless the test is implemented incorrectly, the answer appears to be: "No, Eclipse's $SWITCH_TABLE$ is not thread-safe".
```
*** FAILED tests
  Strong asserts were violated. Correct implementations should have no assert failures here.

  1 matching test results.
  [FAILED] test.EclipseEnumTest
    (JVM args: [-XX:-TieredCompilation, -XX:+UnlockDiagnosticVMOptions, -XX:+StressLCM, -XX:+StressGCM])
  Observed state   Occurrences   Expectation  Interpretation
         0, 0, 1         3,504     FORBIDDEN  Some thread saw a not completely initialized array.
         0, 1, 0         3,595     FORBIDDEN  Some thread saw a not completely initialized array.
         0, 1, 1        43,679     FORBIDDEN  Some thread saw a not completely initialized array.
         1, 0, 0         3,689     FORBIDDEN  Some thread saw a not completely initialized array.
         1, 0, 1        44,730     FORBIDDEN  Some thread saw a not completely initialized array.
         1, 1, 0        44,933     FORBIDDEN  Some thread saw a not completely initialized array.
         1, 1, 1    13,929,831    ACCEPTABLE  Correctly initialized.
```

package test;


import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.III_Result;

/**
 * Lets 3 threads pretend to call {@code Enum.Z.get()} for ecj emulated code.
 */
@JCStressTest
@Outcome(id = "1, 1, 1", expect = Expect.ACCEPTABLE, desc = "Correctly initialized.")
@Outcome(id = ".+, .+, .+", expect = Expect.FORBIDDEN, desc = "Some thread saw a not completely initialized array.")
@State
public class EclipseEnumTest {
    private static final int Z_VALUE = 26;
    
    enum Enum {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
    }
    
    /*
     * Emulate the code ecj would generate when switching on enum values, except make switch table 
     * non-static to allow testing with jcstress 
     * 
     * enum Enum {
     *     A, B, ...;
     *     
     *     public int get() {
     *         switch (this) {
     *             // NOTE: javac would only write value for Z in switch table, however ecj currently 
     *             // writes for all enum values
     *             case Z:
     *                 return 1;
     *             default:
     *                 return 0;
     *         }
     *     }
     * }
     */
    
    private int[] switchTable;
    
    int[] switchTable() {
       final int[] var10000 = switchTable;
       if (var10000 != null) {
          return var10000;
       } else {
          final int[] var0 = new int[Enum.values().length];

          try {
             var0[Enum.A.ordinal()] = 1;
          } catch (final NoSuchFieldError var26) {
          }

          try {
             var0[Enum.B.ordinal()] = 2;
          } catch (final NoSuchFieldError var25) {
          }

          try {
             var0[Enum.C.ordinal()] = 3;
          } catch (final NoSuchFieldError var24) {
          }

          try {
             var0[Enum.D.ordinal()] = 4;
          } catch (final NoSuchFieldError var23) {
          }

          try {
             var0[Enum.E.ordinal()] = 5;
          } catch (final NoSuchFieldError var22) {
          }

          try {
             var0[Enum.F.ordinal()] = 6;
          } catch (final NoSuchFieldError var21) {
          }

          try {
             var0[Enum.G.ordinal()] = 7;
          } catch (final NoSuchFieldError var20) {
          }

          try {
             var0[Enum.H.ordinal()] = 8;
          } catch (final NoSuchFieldError var19) {
          }

          try {
             var0[Enum.I.ordinal()] = 9;
          } catch (final NoSuchFieldError var18) {
          }

          try {
             var0[Enum.J.ordinal()] = 10;
          } catch (final NoSuchFieldError var17) {
          }

          try {
             var0[Enum.K.ordinal()] = 11;
          } catch (final NoSuchFieldError var16) {
          }

          try {
             var0[Enum.L.ordinal()] = 12;
          } catch (final NoSuchFieldError var15) {
          }

          try {
             var0[Enum.M.ordinal()] = 13;
          } catch (final NoSuchFieldError var14) {
          }

          try {
             var0[Enum.N.ordinal()] = 14;
          } catch (final NoSuchFieldError var13) {
          }

          try {
             var0[Enum.O.ordinal()] = 15;
          } catch (final NoSuchFieldError var12) {
          }

          try {
             var0[Enum.P.ordinal()] = 16;
          } catch (final NoSuchFieldError var11) {
          }

          try {
             var0[Enum.Q.ordinal()] = 17;
          } catch (final NoSuchFieldError var10) {
          }

          try {
             var0[Enum.R.ordinal()] = 18;
          } catch (final NoSuchFieldError var9) {
          }

          try {
             var0[Enum.S.ordinal()] = 19;
          } catch (final NoSuchFieldError var8) {
          }

          try {
             var0[Enum.T.ordinal()] = 20;
          } catch (final NoSuchFieldError var7) {
          }

          try {
             var0[Enum.U.ordinal()] = 21;
          } catch (final NoSuchFieldError var6) {
          }

          try {
             var0[Enum.V.ordinal()] = 22;
          } catch (final NoSuchFieldError var5) {
          }

          try {
             var0[Enum.W.ordinal()] = 23;
          } catch (final NoSuchFieldError var4) {
          }

          try {
             var0[Enum.X.ordinal()] = 24;
          } catch (final NoSuchFieldError var3) {
          }

          try {
             var0[Enum.Y.ordinal()] = 25;
          } catch (final NoSuchFieldError var2) {
          }

          try {
             var0[Enum.Z.ordinal()] = Z_VALUE;
          } catch (final NoSuchFieldError var1) {
          }

          switchTable = var0;
          return var0;
       }
    }

    public int get(final Enum e) {
        switch (switchTable()[e.ordinal()]) {
            case Z_VALUE:
                return 1;
            default:
                return 0;
        }
    }
    
    @Actor
    public void test1(final III_Result r) {
        r.r1 = get(Enum.Z);
    }

    @Actor
    public void test2(final III_Result r) {
        r.r2 = get(Enum.Z);
    }

    @Actor
    public void test3(final III_Result r) {
        r.r3 = get(Enum.Z);
    }
}

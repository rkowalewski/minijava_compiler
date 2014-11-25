#include <stdint.h>
#define MEM(x) *((int32_t*)(x))

int32_t Lmain();
int32_t LArr$init(int32_t t2, int32_t t3);
int32_t LArr$sum(int32_t t6);
int32_t LArr$do_it(int32_t t14, int32_t t15);


int32_t Lmain() {
  int32_t t0, t1, t18, t19;
  /* LABEL(L$$25) */
L$$25: ;
  /* MOVE(TEMP(t18), CALL(NAME(L_halloc), OP(*, CONST(2), CONST(4)))) */
  int32_t t22 = (2 * 4);
  int32_t t23 = L_halloc(t22);
  t18 = t23;
  /* MOVE(TEMP(t19), CALL(NAME(LArr$do_it), TEMP(t18), CONST(10))) */
  int32_t t24 = t18;
  int32_t t25 = LArr$do_it(t24, 10);
  t19 = t25;
  /* EXP(CALL(NAME(L_println_int), TEMP(t19))) */
  int32_t t26 = t19;
  int32_t t27 = L_println_int(t26);
  t27;
  /* MOVE(TEMP(t1), CONST(0)) */
  t1 = 0;
  /* MOVE(TEMP(t0), TEMP(t1)) */
  t0 = t1;
  /* LABEL(L$$24) */
L$$24: ;
  return t0;
}

int32_t LArr$init(int32_t t2, int32_t t3) {
  int32_t t0, t4, t5;
  /* LABEL(L$$27) */
L$$27: ;
  /* MOVE(MEM(OP(+, TEMP(t2), CONST(4))), TEMP(t3)) */
  int32_t t28 = t2;
  int32_t t29 = (t28 + 4);
  MEM(t29) = t3;
  /* MOVE(TEMP(t4), CONST(1)) */
  t4 = 1;
  /* LABEL(L$$0) */
L$$0: ;
  /* CJUMP(<, TEMP(t4), OP(+, MEM(OP(+, TEMP(t2), CONST(4))), CONST(1)), L$$2, L$$3) */
  int32_t t30 = t4;
  int32_t t31 = t2;
  int32_t t32 = MEM((t31 + 4));
  int32_t t33 = (t32 + 1);
  if (t30 < t33) goto L$$2; else goto L$$3;
  /* LABEL(L$$3) */
L$$3: ;
  /* MOVE(TEMP(t5), CONST(0)) */
  t5 = 0;
  /* LABEL(L$$4) */
L$$4: ;
  /* CJUMP(==, TEMP(t5), CONST(1), L$$5, L$$1) */
  int32_t t34 = t5;
  if (t34 == 1) goto L$$5; else goto L$$1;
  /* LABEL(L$$1) */
L$$1: ;
  /* MOVE(TEMP(t0), CONST(0)) */
  t0 = 0;
  /* JUMP(NAME(L$$26), [L$$26]) */
  goto L$$26;
  /* LABEL(L$$2) */
L$$2: ;
  /* MOVE(TEMP(t5), CONST(1)) */
  t5 = 1;
  /* JUMP(NAME(L$$4), [L$$4]) */
  goto L$$4;
  /* LABEL(L$$5) */
L$$5: ;
  /* MOVE(MEM(OP(+, MEM(TEMP(t2)), OP(*, OP(+, OP(-, TEMP(t4), CONST(1)), CONST(1)), CONST(4)))), TEMP(t4)) */
  int32_t t35 = MEM(t2);
  int32_t t36 = t4;
  int32_t t37 = (t36 - 1);
  int32_t t38 = (t37 + 1);
  int32_t t39 = (t38 * 4);
  int32_t t40 = (t35 + t39);
  MEM(t40) = t4;
  /* MOVE(TEMP(t4), OP(+, TEMP(t4), CONST(1))) */
  int32_t t41 = t4;
  t4 = (t41 + 1);
  /* JUMP(NAME(L$$0), [L$$0]) */
  goto L$$0;
  /* LABEL(L$$26) */
L$$26: ;
  return t0;
}

int32_t LArr$sum(int32_t t6) {
  int32_t t0, t7, t8, t9, t10, t11, t12, t13, t20;
  /* LABEL(L$$29) */
L$$29: ;
  /* MOVE(TEMP(t7), CONST(1)) */
  t7 = 1;
  /* MOVE(TEMP(t8), CONST(0)) */
  t8 = 0;
  /* LABEL(L$$6) */
L$$6: ;
  /* CJUMP(<, TEMP(t7), OP(+, MEM(OP(+, TEMP(t6), CONST(4))), CONST(1)), L$$8, L$$9) */
  int32_t t42 = t7;
  int32_t t43 = t6;
  int32_t t44 = MEM((t43 + 4));
  int32_t t45 = (t44 + 1);
  if (t42 < t45) goto L$$8; else goto L$$9;
  /* LABEL(L$$9) */
L$$9: ;
  /* MOVE(TEMP(t9), CONST(0)) */
  t9 = 0;
  /* LABEL(L$$10) */
L$$10: ;
  /* CJUMP(==, TEMP(t9), CONST(1), L$$11, L$$7) */
  int32_t t46 = t9;
  if (t46 == 1) goto L$$11; else goto L$$7;
  /* LABEL(L$$7) */
L$$7: ;
  /* MOVE(TEMP(t0), TEMP(t8)) */
  t0 = t8;
  /* JUMP(NAME(L$$28), [L$$28]) */
  goto L$$28;
  /* LABEL(L$$8) */
L$$8: ;
  /* MOVE(TEMP(t9), CONST(1)) */
  t9 = 1;
  /* JUMP(NAME(L$$10), [L$$10]) */
  goto L$$10;
  /* LABEL(L$$11) */
L$$11: ;
  /* MOVE(TEMP(t20), TEMP(t8)) */
  t20 = t8;
  /* CJUMP(<=, CONST(0), OP(-, TEMP(t7), CONST(1)), L$$12, L$$13) */
  int32_t t47 = t7;
  int32_t t48 = (t47 - 1);
  if (0 <= t48) goto L$$12; else goto L$$13;
  /* LABEL(L$$13) */
L$$13: ;
  /* MOVE(TEMP(t10), CONST(0)) */
  t10 = 0;
  /* LABEL(L$$14) */
L$$14: ;
  /* CJUMP(==, TEMP(t10), CONST(0), L$$18, L$$19) */
  int32_t t49 = t10;
  if (t49 == 0) goto L$$18; else goto L$$19;
  /* LABEL(L$$19) */
L$$19: ;
  /* CJUMP(<, OP(-, TEMP(t7), CONST(1)), MEM(MEM(TEMP(t6))), L$$15, L$$16) */
  int32_t t50 = t7;
  int32_t t51 = (t50 - 1);
  int32_t t52 = MEM(MEM(t6));
  if (t51 < t52) goto L$$15; else goto L$$16;
  /* LABEL(L$$16) */
L$$16: ;
  /* MOVE(TEMP(t11), CONST(0)) */
  t11 = 0;
  /* LABEL(L$$17) */
L$$17: ;
  /* MOVE(TEMP(t12), TEMP(t11)) */
  t12 = t11;
  /* LABEL(L$$20) */
L$$20: ;
  /* CJUMP(==, TEMP(t12), CONST(1), L$$21, L$$22) */
  int32_t t53 = t12;
  if (t53 == 1) goto L$$21; else goto L$$22;
  /* LABEL(L$$22) */
L$$22: ;
  /* MOVE(TEMP(t13), CALL(NAME(L_raise), CONST(-1))) */
  int32_t t54 = L_raise(-1);
  t13 = t54;
  /* LABEL(L$$23) */
L$$23: ;
  /* MOVE(TEMP(t8), OP(+, TEMP(t20), TEMP(t13))) */
  int32_t t55 = t20;
  int32_t t56 = t13;
  t8 = (t55 + t56);
  /* MOVE(TEMP(t7), OP(+, TEMP(t7), CONST(1))) */
  int32_t t57 = t7;
  t7 = (t57 + 1);
  /* JUMP(NAME(L$$6), [L$$6]) */
  goto L$$6;
  /* LABEL(L$$12) */
L$$12: ;
  /* MOVE(TEMP(t10), CONST(1)) */
  t10 = 1;
  /* JUMP(NAME(L$$14), [L$$14]) */
  goto L$$14;
  /* LABEL(L$$18) */
L$$18: ;
  /* MOVE(TEMP(t12), CONST(0)) */
  t12 = 0;
  /* JUMP(NAME(L$$20), [L$$20]) */
  goto L$$20;
  /* LABEL(L$$15) */
L$$15: ;
  /* MOVE(TEMP(t11), CONST(1)) */
  t11 = 1;
  /* JUMP(NAME(L$$17), [L$$17]) */
  goto L$$17;
  /* LABEL(L$$21) */
L$$21: ;
  /* MOVE(TEMP(t13), MEM(OP(+, MEM(TEMP(t6)), OP(*, CONST(4), OP(+, OP(-, TEMP(t7), CONST(1)), CONST(1)))))) */
  int32_t t58 = MEM(t6);
  int32_t t59 = t7;
  int32_t t60 = (t59 - 1);
  int32_t t61 = (t60 + 1);
  int32_t t62 = (4 * t61);
  t13 = MEM((t58 + t62));
  /* JUMP(NAME(L$$23), [L$$23]) */
  goto L$$23;
  /* LABEL(L$$28) */
L$$28: ;
  return t0;
}

int32_t LArr$do_it(int32_t t14, int32_t t15) {
  int32_t t0, t16, t17, t21;
  /* LABEL(L$$31) */
L$$31: ;
  /* MOVE(TEMP(t21), TEMP(t14)) */
  t21 = t14;
  /* MOVE(TEMP(t17), CALL(NAME(L_halloc), OP(*, OP(+, TEMP(t15), CONST(1)), CONST(4)))) */
  int32_t t63 = t15;
  int32_t t64 = (t63 + 1);
  int32_t t65 = (t64 * 4);
  int32_t t66 = L_halloc(t65);
  t17 = t66;
  /* MOVE(MEM(TEMP(t17)), TEMP(t15)) */
  int32_t t67 = t17;
  MEM(t67) = t15;
  /* MOVE(MEM(TEMP(t21)), TEMP(t17)) */
  int32_t t68 = t21;
  MEM(t68) = t17;
  /* MOVE(TEMP(t16), CALL(NAME(LArr$init), TEMP(t14), TEMP(t15))) */
  int32_t t69 = t14;
  int32_t t70 = t15;
  int32_t t71 = LArr$init(t69, t70);
  t16 = t71;
  /* MOVE(TEMP(t0), CALL(NAME(LArr$sum), TEMP(t14))) */
  int32_t t72 = t14;
  int32_t t73 = LArr$sum(t72);
  t0 = t73;
  /* LABEL(L$$30) */
L$$30: ;
  return t0;
}



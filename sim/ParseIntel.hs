{-# OPTIONS_GHC -w #-}
module ParseIntel  where

import Data.Int

import GenSym
import Frame -- Acc
import Intel
import TokenIntel hiding (Label, Temp, Reg)
import qualified TokenIntel as Tok
import LexIntel
import FrameIntel

-- parser produced by Happy Version 1.18.9

data HappyAbsSyn 
	= HappyTerminal ((Token AlexPosn))
	| HappyErrorToken Int
	| HappyAbsSyn4 ([IntelFrame])
	| HappyAbsSyn5 ([Acc])
	| HappyAbsSyn7 (Acc)
	| HappyAbsSyn8 ([Instr])
	| HappyAbsSyn9 (Instr)
	| HappyAbsSyn10 (Label)
	| HappyAbsSyn11 (DS)
	| HappyAbsSyn12 (D)
	| HappyAbsSyn13 (Cond)
	| HappyAbsSyn14 (Src)
	| HappyAbsSyn15 (Dest)
	| HappyAbsSyn16 (Reg)
	| HappyAbsSyn17 (EA)
	| HappyAbsSyn18 (Scale)
	| HappyAbsSyn19 (Int32)

{- to allow type-synonyms as our monads (likely
 - with explicitly-specified bind and return)
 - in Haskell98, it seems that with
 - /type M a = .../, then /(HappyReduction M)/
 - is not allowed.  But Happy is a
 - code-generator that can just substitute it.
type HappyReduction m = 
	   Int 
	-> ((Token AlexPosn))
	-> HappyState ((Token AlexPosn)) (HappyStk HappyAbsSyn -> [((Token AlexPosn))] -> m HappyAbsSyn)
	-> [HappyState ((Token AlexPosn)) (HappyStk HappyAbsSyn -> [((Token AlexPosn))] -> m HappyAbsSyn)] 
	-> HappyStk HappyAbsSyn 
	-> [((Token AlexPosn))] -> m HappyAbsSyn
-}

action_0,
 action_1,
 action_2,
 action_3,
 action_4,
 action_5,
 action_6,
 action_7,
 action_8,
 action_9,
 action_10,
 action_11,
 action_12,
 action_13,
 action_14,
 action_15,
 action_16,
 action_17,
 action_18,
 action_19,
 action_20,
 action_21,
 action_22,
 action_23,
 action_24,
 action_25,
 action_26,
 action_27,
 action_28,
 action_29,
 action_30,
 action_31,
 action_32,
 action_33,
 action_34,
 action_35,
 action_36,
 action_37,
 action_38,
 action_39,
 action_40,
 action_41,
 action_42,
 action_43,
 action_44,
 action_45,
 action_46,
 action_47,
 action_48,
 action_49,
 action_50,
 action_51,
 action_52,
 action_53,
 action_54,
 action_55,
 action_56,
 action_57,
 action_58,
 action_59,
 action_60,
 action_61,
 action_62,
 action_63,
 action_64,
 action_65,
 action_66,
 action_67,
 action_68,
 action_69,
 action_70,
 action_71,
 action_72,
 action_73,
 action_74,
 action_75,
 action_76,
 action_77,
 action_78,
 action_79,
 action_80,
 action_81,
 action_82,
 action_83,
 action_84,
 action_85,
 action_86,
 action_87,
 action_88,
 action_89,
 action_90,
 action_91,
 action_92,
 action_93,
 action_94,
 action_95,
 action_96,
 action_97,
 action_98,
 action_99,
 action_100,
 action_101,
 action_102,
 action_103,
 action_104,
 action_105,
 action_106,
 action_107,
 action_108,
 action_109,
 action_110,
 action_111,
 action_112,
 action_113,
 action_114,
 action_115,
 action_116,
 action_117,
 action_118,
 action_119,
 action_120,
 action_121,
 action_122,
 action_123,
 action_124,
 action_125,
 action_126,
 action_127,
 action_128,
 action_129,
 action_130,
 action_131,
 action_132,
 action_133,
 action_134,
 action_135,
 action_136,
 action_137,
 action_138,
 action_139,
 action_140,
 action_141,
 action_142,
 action_143,
 action_144,
 action_145,
 action_146,
 action_147,
 action_148,
 action_149,
 action_150,
 action_151,
 action_152,
 action_153,
 action_154,
 action_155,
 action_156,
 action_157,
 action_158,
 action_159,
 action_160,
 action_161,
 action_162,
 action_163,
 action_164,
 action_165,
 action_166,
 action_167,
 action_168,
 action_169,
 action_170,
 action_171,
 action_172,
 action_173,
 action_174,
 action_175,
 action_176,
 action_177,
 action_178,
 action_179,
 action_180,
 action_181,
 action_182,
 action_183,
 action_184,
 action_185,
 action_186,
 action_187,
 action_188,
 action_189,
 action_190,
 action_191,
 action_192 :: () => Int -> ({-HappyReduction (HappyIdentity) = -}
	   Int 
	-> ((Token AlexPosn))
	-> HappyState ((Token AlexPosn)) (HappyStk HappyAbsSyn -> [((Token AlexPosn))] -> (HappyIdentity) HappyAbsSyn)
	-> [HappyState ((Token AlexPosn)) (HappyStk HappyAbsSyn -> [((Token AlexPosn))] -> (HappyIdentity) HappyAbsSyn)] 
	-> HappyStk HappyAbsSyn 
	-> [((Token AlexPosn))] -> (HappyIdentity) HappyAbsSyn)

happyReduce_1,
 happyReduce_2,
 happyReduce_3,
 happyReduce_4,
 happyReduce_5,
 happyReduce_6,
 happyReduce_7,
 happyReduce_8,
 happyReduce_9,
 happyReduce_10,
 happyReduce_11,
 happyReduce_12,
 happyReduce_13,
 happyReduce_14,
 happyReduce_15,
 happyReduce_16,
 happyReduce_17,
 happyReduce_18,
 happyReduce_19,
 happyReduce_20,
 happyReduce_21,
 happyReduce_22,
 happyReduce_23,
 happyReduce_24,
 happyReduce_25,
 happyReduce_26,
 happyReduce_27,
 happyReduce_28,
 happyReduce_29,
 happyReduce_30,
 happyReduce_31,
 happyReduce_32,
 happyReduce_33,
 happyReduce_34,
 happyReduce_35,
 happyReduce_36,
 happyReduce_37,
 happyReduce_38,
 happyReduce_39,
 happyReduce_40,
 happyReduce_41,
 happyReduce_42,
 happyReduce_43,
 happyReduce_44,
 happyReduce_45,
 happyReduce_46,
 happyReduce_47,
 happyReduce_48,
 happyReduce_49,
 happyReduce_50,
 happyReduce_51,
 happyReduce_52,
 happyReduce_53,
 happyReduce_54,
 happyReduce_55,
 happyReduce_56,
 happyReduce_57,
 happyReduce_58,
 happyReduce_59,
 happyReduce_60,
 happyReduce_61,
 happyReduce_62,
 happyReduce_63,
 happyReduce_64,
 happyReduce_65,
 happyReduce_66,
 happyReduce_67,
 happyReduce_68,
 happyReduce_69,
 happyReduce_70,
 happyReduce_71,
 happyReduce_72,
 happyReduce_73,
 happyReduce_74,
 happyReduce_75,
 happyReduce_76,
 happyReduce_77,
 happyReduce_78,
 happyReduce_79,
 happyReduce_80,
 happyReduce_81,
 happyReduce_82,
 happyReduce_83,
 happyReduce_84,
 happyReduce_85,
 happyReduce_86,
 happyReduce_87,
 happyReduce_88,
 happyReduce_89,
 happyReduce_90,
 happyReduce_91,
 happyReduce_92,
 happyReduce_93,
 happyReduce_94,
 happyReduce_95,
 happyReduce_96,
 happyReduce_97,
 happyReduce_98,
 happyReduce_99,
 happyReduce_100,
 happyReduce_101,
 happyReduce_102,
 happyReduce_103,
 happyReduce_104,
 happyReduce_105,
 happyReduce_106,
 happyReduce_107 :: () => ({-HappyReduction (HappyIdentity) = -}
	   Int 
	-> ((Token AlexPosn))
	-> HappyState ((Token AlexPosn)) (HappyStk HappyAbsSyn -> [((Token AlexPosn))] -> (HappyIdentity) HappyAbsSyn)
	-> [HappyState ((Token AlexPosn)) (HappyStk HappyAbsSyn -> [((Token AlexPosn))] -> (HappyIdentity) HappyAbsSyn)] 
	-> HappyStk HappyAbsSyn 
	-> [((Token AlexPosn))] -> (HappyIdentity) HappyAbsSyn)

action_0 (73) = happyShift action_4
action_0 (4) = happyGoto action_2
action_0 (10) = happyGoto action_3
action_0 _ = happyReduce_1

action_1 _ = happyFail

action_2 (74) = happyAccept
action_2 _ = happyFail

action_3 (33) = happyShift action_7
action_3 (5) = happyGoto action_6
action_3 _ = happyReduce_4

action_4 (23) = happyShift action_5
action_4 _ = happyFail

action_5 _ = happyReduce_28

action_6 (36) = happyShift action_18
action_6 (37) = happyShift action_19
action_6 (38) = happyShift action_20
action_6 (39) = happyShift action_21
action_6 (40) = happyShift action_22
action_6 (41) = happyShift action_23
action_6 (42) = happyShift action_24
action_6 (43) = happyShift action_25
action_6 (44) = happyShift action_26
action_6 (45) = happyShift action_27
action_6 (46) = happyShift action_28
action_6 (47) = happyShift action_29
action_6 (48) = happyShift action_30
action_6 (49) = happyShift action_31
action_6 (50) = happyShift action_32
action_6 (51) = happyShift action_33
action_6 (52) = happyShift action_34
action_6 (53) = happyShift action_35
action_6 (54) = happyShift action_36
action_6 (55) = happyShift action_37
action_6 (56) = happyShift action_38
action_6 (57) = happyShift action_39
action_6 (58) = happyShift action_40
action_6 (59) = happyShift action_41
action_6 (60) = happyShift action_42
action_6 (61) = happyShift action_43
action_6 (62) = happyShift action_44
action_6 (63) = happyShift action_45
action_6 (65) = happyShift action_46
action_6 (66) = happyShift action_47
action_6 (67) = happyShift action_48
action_6 (73) = happyShift action_4
action_6 (8) = happyGoto action_12
action_6 (9) = happyGoto action_13
action_6 (10) = happyGoto action_14
action_6 (11) = happyGoto action_15
action_6 (12) = happyGoto action_16
action_6 (13) = happyGoto action_17
action_6 _ = happyReduce_11

action_7 (34) = happyShift action_10
action_7 (35) = happyShift action_11
action_7 (6) = happyGoto action_8
action_7 (7) = happyGoto action_9
action_7 _ = happyReduce_5

action_8 _ = happyReduce_6

action_9 (24) = happyShift action_78
action_9 _ = happyReduce_7

action_10 (29) = happyShift action_50
action_10 (30) = happyShift action_51
action_10 (31) = happyShift action_52
action_10 (32) = happyShift action_53
action_10 (70) = happyShift action_54
action_10 (19) = happyGoto action_77
action_10 _ = happyFail

action_11 (71) = happyShift action_76
action_11 _ = happyFail

action_12 (64) = happyShift action_75
action_12 _ = happyReduce_2

action_13 (36) = happyShift action_18
action_13 (37) = happyShift action_19
action_13 (38) = happyShift action_20
action_13 (39) = happyShift action_21
action_13 (40) = happyShift action_22
action_13 (41) = happyShift action_23
action_13 (42) = happyShift action_24
action_13 (43) = happyShift action_25
action_13 (44) = happyShift action_26
action_13 (45) = happyShift action_27
action_13 (46) = happyShift action_28
action_13 (47) = happyShift action_29
action_13 (48) = happyShift action_30
action_13 (49) = happyShift action_31
action_13 (50) = happyShift action_32
action_13 (51) = happyShift action_33
action_13 (52) = happyShift action_34
action_13 (53) = happyShift action_35
action_13 (54) = happyShift action_36
action_13 (55) = happyShift action_37
action_13 (56) = happyShift action_38
action_13 (57) = happyShift action_39
action_13 (58) = happyShift action_40
action_13 (59) = happyShift action_41
action_13 (60) = happyShift action_42
action_13 (61) = happyShift action_43
action_13 (62) = happyShift action_44
action_13 (63) = happyShift action_45
action_13 (65) = happyShift action_46
action_13 (66) = happyShift action_47
action_13 (67) = happyShift action_48
action_13 (73) = happyShift action_4
action_13 (8) = happyGoto action_74
action_13 (9) = happyGoto action_13
action_13 (10) = happyGoto action_14
action_13 (11) = happyGoto action_15
action_13 (12) = happyGoto action_16
action_13 (13) = happyGoto action_17
action_13 _ = happyReduce_11

action_14 _ = happyReduce_13

action_15 (68) = happyShift action_58
action_15 (71) = happyShift action_59
action_15 (72) = happyShift action_60
action_15 (15) = happyGoto action_73
action_15 (16) = happyGoto action_57
action_15 _ = happyFail

action_16 (68) = happyShift action_58
action_16 (71) = happyShift action_59
action_16 (72) = happyShift action_60
action_16 (15) = happyGoto action_72
action_16 (16) = happyGoto action_57
action_16 _ = happyFail

action_17 (73) = happyShift action_71
action_17 _ = happyFail

action_18 _ = happyReduce_29

action_19 _ = happyReduce_30

action_20 _ = happyReduce_31

action_21 _ = happyReduce_32

action_22 _ = happyReduce_33

action_23 _ = happyReduce_34

action_24 _ = happyReduce_35

action_25 _ = happyReduce_36

action_26 _ = happyReduce_37

action_27 _ = happyReduce_38

action_28 _ = happyReduce_40

action_29 _ = happyReduce_41

action_30 _ = happyReduce_42

action_31 _ = happyReduce_43

action_32 _ = happyReduce_39

action_33 (27) = happyShift action_70
action_33 (29) = happyShift action_50
action_33 (30) = happyShift action_51
action_33 (31) = happyShift action_52
action_33 (32) = happyShift action_53
action_33 (68) = happyShift action_58
action_33 (70) = happyShift action_54
action_33 (71) = happyShift action_59
action_33 (72) = happyShift action_60
action_33 (14) = happyGoto action_66
action_33 (15) = happyGoto action_67
action_33 (16) = happyGoto action_57
action_33 (19) = happyGoto action_68
action_33 (20) = happyGoto action_69
action_33 _ = happyFail

action_34 (68) = happyShift action_58
action_34 (71) = happyShift action_59
action_34 (72) = happyShift action_60
action_34 (15) = happyGoto action_64
action_34 (16) = happyGoto action_65
action_34 _ = happyFail

action_35 (68) = happyShift action_58
action_35 (71) = happyShift action_59
action_35 (72) = happyShift action_60
action_35 (15) = happyGoto action_63
action_35 (16) = happyGoto action_57
action_35 _ = happyFail

action_36 (73) = happyShift action_62
action_36 _ = happyFail

action_37 (71) = happyShift action_59
action_37 (72) = happyShift action_60
action_37 (16) = happyGoto action_61
action_37 _ = happyFail

action_38 (68) = happyShift action_58
action_38 (71) = happyShift action_59
action_38 (72) = happyShift action_60
action_38 (15) = happyGoto action_56
action_38 (16) = happyGoto action_57
action_38 _ = happyFail

action_39 _ = happyReduce_44

action_40 _ = happyReduce_45

action_41 _ = happyReduce_46

action_42 _ = happyReduce_47

action_43 _ = happyReduce_48

action_44 _ = happyReduce_49

action_45 (73) = happyShift action_55
action_45 _ = happyFail

action_46 (29) = happyShift action_50
action_46 (30) = happyShift action_51
action_46 (31) = happyShift action_52
action_46 (32) = happyShift action_53
action_46 (70) = happyShift action_54
action_46 (19) = happyGoto action_49
action_46 _ = happyFail

action_47 _ = happyReduce_26

action_48 _ = happyReduce_27

action_49 (24) = happyShift action_87
action_49 _ = happyFail

action_50 _ = happyReduce_101

action_51 _ = happyReduce_102

action_52 _ = happyReduce_103

action_53 _ = happyReduce_104

action_54 _ = happyReduce_105

action_55 _ = happyReduce_24

action_56 (24) = happyShift action_86
action_56 _ = happyFail

action_57 _ = happyReduce_52

action_58 (69) = happyShift action_85
action_58 _ = happyFail

action_59 _ = happyReduce_55

action_60 _ = happyReduce_54

action_61 (24) = happyShift action_84
action_61 _ = happyFail

action_62 _ = happyReduce_22

action_63 _ = happyReduce_21

action_64 _ = happyReduce_20

action_65 (24) = happyShift action_83
action_65 _ = happyReduce_52

action_66 _ = happyReduce_18

action_67 _ = happyReduce_51

action_68 _ = happyReduce_106

action_69 _ = happyReduce_50

action_70 (29) = happyShift action_50
action_70 (30) = happyShift action_51
action_70 (31) = happyShift action_52
action_70 (32) = happyShift action_53
action_70 (70) = happyShift action_54
action_70 (19) = happyGoto action_82
action_70 _ = happyFail

action_71 _ = happyReduce_23

action_72 _ = happyReduce_15

action_73 (24) = happyShift action_81
action_73 _ = happyFail

action_74 _ = happyReduce_12

action_75 (73) = happyShift action_4
action_75 (4) = happyGoto action_80
action_75 (10) = happyGoto action_3
action_75 _ = happyReduce_1

action_76 _ = happyReduce_10

action_77 _ = happyReduce_9

action_78 (34) = happyShift action_10
action_78 (35) = happyShift action_11
action_78 (6) = happyGoto action_79
action_78 (7) = happyGoto action_9
action_78 _ = happyFail

action_79 _ = happyReduce_8

action_80 _ = happyReduce_3

action_81 (27) = happyShift action_70
action_81 (29) = happyShift action_50
action_81 (30) = happyShift action_51
action_81 (31) = happyShift action_52
action_81 (32) = happyShift action_53
action_81 (68) = happyShift action_58
action_81 (70) = happyShift action_54
action_81 (71) = happyShift action_59
action_81 (72) = happyShift action_60
action_81 (14) = happyGoto action_93
action_81 (15) = happyGoto action_67
action_81 (16) = happyGoto action_57
action_81 (19) = happyGoto action_68
action_81 (20) = happyGoto action_69
action_81 _ = happyFail

action_82 _ = happyReduce_107

action_83 (68) = happyShift action_58
action_83 (71) = happyShift action_59
action_83 (72) = happyShift action_60
action_83 (15) = happyGoto action_92
action_83 (16) = happyGoto action_57
action_83 _ = happyFail

action_84 (68) = happyShift action_91
action_84 _ = happyFail

action_85 (21) = happyShift action_90
action_85 _ = happyFail

action_86 (27) = happyShift action_70
action_86 (29) = happyShift action_50
action_86 (30) = happyShift action_51
action_86 (31) = happyShift action_52
action_86 (32) = happyShift action_53
action_86 (68) = happyShift action_58
action_86 (70) = happyShift action_54
action_86 (71) = happyShift action_59
action_86 (72) = happyShift action_60
action_86 (14) = happyGoto action_89
action_86 (15) = happyGoto action_67
action_86 (16) = happyGoto action_57
action_86 (19) = happyGoto action_68
action_86 (20) = happyGoto action_69
action_86 _ = happyFail

action_87 (29) = happyShift action_88
action_87 _ = happyFail

action_88 _ = happyReduce_25

action_89 _ = happyReduce_17

action_90 (27) = happyShift action_70
action_90 (29) = happyShift action_50
action_90 (30) = happyShift action_99
action_90 (31) = happyShift action_100
action_90 (32) = happyShift action_101
action_90 (70) = happyShift action_54
action_90 (71) = happyShift action_59
action_90 (72) = happyShift action_60
action_90 (16) = happyGoto action_95
action_90 (17) = happyGoto action_96
action_90 (18) = happyGoto action_97
action_90 (19) = happyGoto action_68
action_90 (20) = happyGoto action_98
action_90 _ = happyFail

action_91 (69) = happyShift action_94
action_91 _ = happyFail

action_92 _ = happyReduce_19

action_93 _ = happyReduce_14

action_94 (21) = happyShift action_108
action_94 _ = happyFail

action_95 (26) = happyShift action_105
action_95 (27) = happyShift action_106
action_95 (28) = happyShift action_107
action_95 _ = happyReduce_56

action_96 (22) = happyShift action_104
action_96 _ = happyFail

action_97 (28) = happyShift action_103
action_97 _ = happyFail

action_98 (26) = happyShift action_102
action_98 _ = happyFail

action_99 (28) = happyReduce_98
action_99 _ = happyReduce_102

action_100 (28) = happyReduce_99
action_100 _ = happyReduce_103

action_101 (28) = happyReduce_100
action_101 _ = happyReduce_104

action_102 (30) = happyShift action_111
action_102 (31) = happyShift action_112
action_102 (32) = happyShift action_113
action_102 (71) = happyShift action_59
action_102 (72) = happyShift action_60
action_102 (16) = happyGoto action_119
action_102 (18) = happyGoto action_120
action_102 _ = happyFail

action_103 (71) = happyShift action_59
action_103 (72) = happyShift action_60
action_103 (16) = happyGoto action_118
action_103 _ = happyFail

action_104 _ = happyReduce_53

action_105 (29) = happyShift action_50
action_105 (30) = happyShift action_99
action_105 (31) = happyShift action_100
action_105 (32) = happyShift action_101
action_105 (70) = happyShift action_54
action_105 (71) = happyShift action_59
action_105 (72) = happyShift action_60
action_105 (16) = happyGoto action_115
action_105 (18) = happyGoto action_116
action_105 (19) = happyGoto action_117
action_105 _ = happyFail

action_106 (29) = happyShift action_50
action_106 (30) = happyShift action_51
action_106 (31) = happyShift action_52
action_106 (32) = happyShift action_53
action_106 (70) = happyShift action_54
action_106 (19) = happyGoto action_114
action_106 _ = happyFail

action_107 (30) = happyShift action_111
action_107 (31) = happyShift action_112
action_107 (32) = happyShift action_113
action_107 (18) = happyGoto action_110
action_107 _ = happyFail

action_108 (27) = happyShift action_70
action_108 (29) = happyShift action_50
action_108 (30) = happyShift action_99
action_108 (31) = happyShift action_100
action_108 (32) = happyShift action_101
action_108 (70) = happyShift action_54
action_108 (71) = happyShift action_59
action_108 (72) = happyShift action_60
action_108 (16) = happyGoto action_95
action_108 (17) = happyGoto action_109
action_108 (18) = happyGoto action_97
action_108 (19) = happyGoto action_68
action_108 (20) = happyGoto action_98
action_108 _ = happyFail

action_109 (22) = happyShift action_134
action_109 _ = happyFail

action_110 (26) = happyShift action_132
action_110 (27) = happyShift action_133
action_110 _ = happyReduce_63

action_111 _ = happyReduce_98

action_112 _ = happyReduce_99

action_113 _ = happyReduce_100

action_114 (26) = happyShift action_131
action_114 _ = happyReduce_85

action_115 (26) = happyShift action_128
action_115 (27) = happyShift action_129
action_115 (28) = happyShift action_130
action_115 _ = happyReduce_58

action_116 (28) = happyShift action_127
action_116 _ = happyFail

action_117 (26) = happyShift action_126
action_117 _ = happyReduce_57

action_118 (26) = happyShift action_124
action_118 (27) = happyShift action_125
action_118 _ = happyReduce_64

action_119 (26) = happyShift action_122
action_119 (28) = happyShift action_123
action_119 _ = happyReduce_59

action_120 (28) = happyShift action_121
action_120 _ = happyFail

action_121 (71) = happyShift action_59
action_121 (72) = happyShift action_60
action_121 (16) = happyGoto action_152
action_121 _ = happyFail

action_122 (30) = happyShift action_111
action_122 (31) = happyShift action_112
action_122 (32) = happyShift action_113
action_122 (71) = happyShift action_59
action_122 (72) = happyShift action_60
action_122 (16) = happyGoto action_150
action_122 (18) = happyGoto action_151
action_122 _ = happyFail

action_123 (30) = happyShift action_111
action_123 (31) = happyShift action_112
action_123 (32) = happyShift action_113
action_123 (18) = happyGoto action_149
action_123 _ = happyFail

action_124 (29) = happyShift action_50
action_124 (30) = happyShift action_51
action_124 (31) = happyShift action_52
action_124 (32) = happyShift action_53
action_124 (70) = happyShift action_54
action_124 (71) = happyShift action_59
action_124 (72) = happyShift action_60
action_124 (16) = happyGoto action_147
action_124 (19) = happyGoto action_148
action_124 _ = happyFail

action_125 (29) = happyShift action_50
action_125 (30) = happyShift action_51
action_125 (31) = happyShift action_52
action_125 (32) = happyShift action_53
action_125 (70) = happyShift action_54
action_125 (19) = happyGoto action_146
action_125 _ = happyFail

action_126 (30) = happyShift action_111
action_126 (31) = happyShift action_112
action_126 (32) = happyShift action_113
action_126 (71) = happyShift action_59
action_126 (72) = happyShift action_60
action_126 (16) = happyGoto action_144
action_126 (18) = happyGoto action_145
action_126 _ = happyFail

action_127 (71) = happyShift action_59
action_127 (72) = happyShift action_60
action_127 (16) = happyGoto action_143
action_127 _ = happyFail

action_128 (29) = happyShift action_50
action_128 (30) = happyShift action_51
action_128 (31) = happyShift action_52
action_128 (32) = happyShift action_53
action_128 (70) = happyShift action_54
action_128 (19) = happyGoto action_142
action_128 _ = happyFail

action_129 (29) = happyShift action_50
action_129 (30) = happyShift action_51
action_129 (31) = happyShift action_52
action_129 (32) = happyShift action_53
action_129 (70) = happyShift action_54
action_129 (19) = happyGoto action_141
action_129 _ = happyFail

action_130 (30) = happyShift action_111
action_130 (31) = happyShift action_112
action_130 (32) = happyShift action_113
action_130 (18) = happyGoto action_140
action_130 _ = happyFail

action_131 (30) = happyShift action_111
action_131 (31) = happyShift action_112
action_131 (32) = happyShift action_113
action_131 (71) = happyShift action_59
action_131 (72) = happyShift action_60
action_131 (16) = happyGoto action_138
action_131 (18) = happyGoto action_139
action_131 _ = happyFail

action_132 (29) = happyShift action_50
action_132 (30) = happyShift action_51
action_132 (31) = happyShift action_52
action_132 (32) = happyShift action_53
action_132 (70) = happyShift action_54
action_132 (71) = happyShift action_59
action_132 (72) = happyShift action_60
action_132 (16) = happyGoto action_136
action_132 (19) = happyGoto action_137
action_132 _ = happyFail

action_133 (29) = happyShift action_50
action_133 (30) = happyShift action_51
action_133 (31) = happyShift action_52
action_133 (32) = happyShift action_53
action_133 (70) = happyShift action_54
action_133 (19) = happyGoto action_135
action_133 _ = happyFail

action_134 _ = happyReduce_16

action_135 (26) = happyShift action_172
action_135 _ = happyReduce_88

action_136 (26) = happyShift action_170
action_136 (27) = happyShift action_171
action_136 _ = happyReduce_67

action_137 (26) = happyShift action_169
action_137 _ = happyReduce_65

action_138 (28) = happyShift action_168
action_138 _ = happyReduce_87

action_139 (28) = happyShift action_167
action_139 _ = happyFail

action_140 (26) = happyShift action_165
action_140 (27) = happyShift action_166
action_140 _ = happyReduce_71

action_141 _ = happyReduce_86

action_142 _ = happyReduce_60

action_143 (26) = happyShift action_163
action_143 (27) = happyShift action_164
action_143 _ = happyReduce_72

action_144 (28) = happyShift action_162
action_144 _ = happyReduce_61

action_145 (28) = happyShift action_161
action_145 _ = happyFail

action_146 (26) = happyShift action_160
action_146 _ = happyReduce_89

action_147 (26) = happyShift action_158
action_147 (27) = happyShift action_159
action_147 _ = happyReduce_68

action_148 (26) = happyShift action_157
action_148 _ = happyReduce_66

action_149 (26) = happyShift action_156
action_149 _ = happyReduce_69

action_150 (28) = happyShift action_155
action_150 _ = happyReduce_62

action_151 (28) = happyShift action_154
action_151 _ = happyFail

action_152 (26) = happyShift action_153
action_152 _ = happyReduce_70

action_153 (71) = happyShift action_59
action_153 (72) = happyShift action_60
action_153 (16) = happyGoto action_192
action_153 _ = happyFail

action_154 (71) = happyShift action_59
action_154 (72) = happyShift action_60
action_154 (16) = happyGoto action_191
action_154 _ = happyFail

action_155 (30) = happyShift action_111
action_155 (31) = happyShift action_112
action_155 (32) = happyShift action_113
action_155 (18) = happyGoto action_190
action_155 _ = happyFail

action_156 (71) = happyShift action_59
action_156 (72) = happyShift action_60
action_156 (16) = happyGoto action_189
action_156 _ = happyFail

action_157 (71) = happyShift action_59
action_157 (72) = happyShift action_60
action_157 (16) = happyGoto action_188
action_157 _ = happyFail

action_158 (29) = happyShift action_50
action_158 (30) = happyShift action_51
action_158 (31) = happyShift action_52
action_158 (32) = happyShift action_53
action_158 (70) = happyShift action_54
action_158 (19) = happyGoto action_187
action_158 _ = happyFail

action_159 (29) = happyShift action_50
action_159 (30) = happyShift action_51
action_159 (31) = happyShift action_52
action_159 (32) = happyShift action_53
action_159 (70) = happyShift action_54
action_159 (19) = happyGoto action_186
action_159 _ = happyFail

action_160 (71) = happyShift action_59
action_160 (72) = happyShift action_60
action_160 (16) = happyGoto action_185
action_160 _ = happyFail

action_161 (71) = happyShift action_59
action_161 (72) = happyShift action_60
action_161 (16) = happyGoto action_184
action_161 _ = happyFail

action_162 (30) = happyShift action_111
action_162 (31) = happyShift action_112
action_162 (32) = happyShift action_113
action_162 (18) = happyGoto action_183
action_162 _ = happyFail

action_163 (29) = happyShift action_50
action_163 (30) = happyShift action_51
action_163 (31) = happyShift action_52
action_163 (32) = happyShift action_53
action_163 (70) = happyShift action_54
action_163 (19) = happyGoto action_182
action_163 _ = happyFail

action_164 (29) = happyShift action_50
action_164 (30) = happyShift action_51
action_164 (31) = happyShift action_52
action_164 (32) = happyShift action_53
action_164 (70) = happyShift action_54
action_164 (19) = happyGoto action_181
action_164 _ = happyFail

action_165 (29) = happyShift action_50
action_165 (30) = happyShift action_51
action_165 (31) = happyShift action_52
action_165 (32) = happyShift action_53
action_165 (70) = happyShift action_54
action_165 (19) = happyGoto action_180
action_165 _ = happyFail

action_166 (29) = happyShift action_50
action_166 (30) = happyShift action_51
action_166 (31) = happyShift action_52
action_166 (32) = happyShift action_53
action_166 (70) = happyShift action_54
action_166 (19) = happyGoto action_179
action_166 _ = happyFail

action_167 (71) = happyShift action_59
action_167 (72) = happyShift action_60
action_167 (16) = happyGoto action_178
action_167 _ = happyFail

action_168 (30) = happyShift action_111
action_168 (31) = happyShift action_112
action_168 (32) = happyShift action_113
action_168 (18) = happyGoto action_177
action_168 _ = happyFail

action_169 (71) = happyShift action_59
action_169 (72) = happyShift action_60
action_169 (16) = happyGoto action_176
action_169 _ = happyFail

action_170 (29) = happyShift action_50
action_170 (30) = happyShift action_51
action_170 (31) = happyShift action_52
action_170 (32) = happyShift action_53
action_170 (70) = happyShift action_54
action_170 (19) = happyGoto action_175
action_170 _ = happyFail

action_171 (29) = happyShift action_50
action_171 (30) = happyShift action_51
action_171 (31) = happyShift action_52
action_171 (32) = happyShift action_53
action_171 (70) = happyShift action_54
action_171 (19) = happyGoto action_174
action_171 _ = happyFail

action_172 (71) = happyShift action_59
action_172 (72) = happyShift action_60
action_172 (16) = happyGoto action_173
action_172 _ = happyFail

action_173 _ = happyReduce_90

action_174 _ = happyReduce_92

action_175 _ = happyReduce_75

action_176 _ = happyReduce_73

action_177 _ = happyReduce_96

action_178 _ = happyReduce_97

action_179 _ = happyReduce_94

action_180 _ = happyReduce_79

action_181 _ = happyReduce_95

action_182 _ = happyReduce_80

action_183 _ = happyReduce_81

action_184 _ = happyReduce_82

action_185 _ = happyReduce_91

action_186 _ = happyReduce_93

action_187 _ = happyReduce_76

action_188 _ = happyReduce_74

action_189 _ = happyReduce_77

action_190 _ = happyReduce_83

action_191 _ = happyReduce_84

action_192 _ = happyReduce_78

happyReduce_1 = happySpecReduce_0  4 happyReduction_1
happyReduction_1  =  HappyAbsSyn4
		 ([]
	)

happyReduce_2 = happySpecReduce_3  4 happyReduction_2
happyReduction_2 (HappyAbsSyn8  happy_var_3)
	(HappyAbsSyn5  happy_var_2)
	(HappyAbsSyn10  happy_var_1)
	 =  HappyAbsSyn4
		 ([Frame happy_var_1 (mkFrameData happy_var_2) happy_var_3]
	)
happyReduction_2 _ _ _  = notHappyAtAll 

happyReduce_3 = happyReduce 5 4 happyReduction_3
happyReduction_3 ((HappyAbsSyn4  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn8  happy_var_3) `HappyStk`
	(HappyAbsSyn5  happy_var_2) `HappyStk`
	(HappyAbsSyn10  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn4
		 ((Frame happy_var_1 (mkFrameData happy_var_2) (happy_var_3 ++ [RET])) : happy_var_5
	) `HappyStk` happyRest

happyReduce_4 = happySpecReduce_0  5 happyReduction_4
happyReduction_4  =  HappyAbsSyn5
		 ([]
	)

happyReduce_5 = happySpecReduce_1  5 happyReduction_5
happyReduction_5 _
	 =  HappyAbsSyn5
		 ([]
	)

happyReduce_6 = happySpecReduce_2  5 happyReduction_6
happyReduction_6 (HappyAbsSyn5  happy_var_2)
	_
	 =  HappyAbsSyn5
		 (happy_var_2
	)
happyReduction_6 _ _  = notHappyAtAll 

happyReduce_7 = happySpecReduce_1  6 happyReduction_7
happyReduction_7 (HappyAbsSyn7  happy_var_1)
	 =  HappyAbsSyn5
		 ([ happy_var_1 ]
	)
happyReduction_7 _  = notHappyAtAll 

happyReduce_8 = happySpecReduce_3  6 happyReduction_8
happyReduction_8 (HappyAbsSyn5  happy_var_3)
	_
	(HappyAbsSyn7  happy_var_1)
	 =  HappyAbsSyn5
		 (happy_var_1 : happy_var_3
	)
happyReduction_8 _ _ _  = notHappyAtAll 

happyReduce_9 = happySpecReduce_2  7 happyReduction_9
happyReduction_9 (HappyAbsSyn19  happy_var_2)
	_
	 =  HappyAbsSyn7
		 (InFrame (fromIntegral happy_var_2)
	)
happyReduction_9 _ _  = notHappyAtAll 

happyReduce_10 = happySpecReduce_2  7 happyReduction_10
happyReduction_10 (HappyTerminal (Tok.Temp happy_var_2 _))
	_
	 =  HappyAbsSyn7
		 (InReg (Temp happy_var_2)
	)
happyReduction_10 _ _  = notHappyAtAll 

happyReduce_11 = happySpecReduce_0  8 happyReduction_11
happyReduction_11  =  HappyAbsSyn8
		 ([ ]
	)

happyReduce_12 = happySpecReduce_2  8 happyReduction_12
happyReduction_12 (HappyAbsSyn8  happy_var_2)
	(HappyAbsSyn9  happy_var_1)
	 =  HappyAbsSyn8
		 (happy_var_1 : happy_var_2
	)
happyReduction_12 _ _  = notHappyAtAll 

happyReduce_13 = happySpecReduce_1  9 happyReduction_13
happyReduction_13 (HappyAbsSyn10  happy_var_1)
	 =  HappyAbsSyn9
		 (LABEL happy_var_1
	)
happyReduction_13 _  = notHappyAtAll 

happyReduce_14 = happyReduce 4 9 happyReduction_14
happyReduction_14 ((HappyAbsSyn14  happy_var_4) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn15  happy_var_2) `HappyStk`
	(HappyAbsSyn11  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn9
		 (DS happy_var_1 happy_var_2 happy_var_4
	) `HappyStk` happyRest

happyReduce_15 = happySpecReduce_2  9 happyReduction_15
happyReduction_15 (HappyAbsSyn15  happy_var_2)
	(HappyAbsSyn12  happy_var_1)
	 =  HappyAbsSyn9
		 (D  happy_var_1 happy_var_2
	)
happyReduction_15 _ _  = notHappyAtAll 

happyReduce_16 = happyReduce 8 9 happyReduction_16
happyReduction_16 (_ `HappyStk`
	(HappyAbsSyn17  happy_var_7) `HappyStk`
	_ `HappyStk`
	_ `HappyStk`
	_ `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_2) `HappyStk`
	_ `HappyStk`
	happyRest)
	 = HappyAbsSyn9
		 (LEA happy_var_2 happy_var_7
	) `HappyStk` happyRest

happyReduce_17 = happyReduce 4 9 happyReduction_17
happyReduction_17 ((HappyAbsSyn14  happy_var_4) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn15  happy_var_2) `HappyStk`
	_ `HappyStk`
	happyRest)
	 = HappyAbsSyn9
		 (CMP happy_var_2 happy_var_4
	) `HappyStk` happyRest

happyReduce_18 = happySpecReduce_2  9 happyReduction_18
happyReduction_18 (HappyAbsSyn14  happy_var_2)
	_
	 =  HappyAbsSyn9
		 (PUSH happy_var_2
	)
happyReduction_18 _ _  = notHappyAtAll 

happyReduce_19 = happyReduce 4 9 happyReduction_19
happyReduction_19 ((HappyAbsSyn15  happy_var_4) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_2) `HappyStk`
	_ `HappyStk`
	happyRest)
	 = HappyAbsSyn9
		 (DS IMUL2 (Reg happy_var_2) (Dest happy_var_4)
	) `HappyStk` happyRest

happyReduce_20 = happySpecReduce_2  9 happyReduction_20
happyReduction_20 (HappyAbsSyn15  happy_var_2)
	_
	 =  HappyAbsSyn9
		 (IMUL happy_var_2
	)
happyReduction_20 _ _  = notHappyAtAll 

happyReduce_21 = happySpecReduce_2  9 happyReduction_21
happyReduction_21 (HappyAbsSyn15  happy_var_2)
	_
	 =  HappyAbsSyn9
		 (IDIV happy_var_2
	)
happyReduction_21 _ _  = notHappyAtAll 

happyReduce_22 = happySpecReduce_2  9 happyReduction_22
happyReduction_22 (HappyTerminal (Id happy_var_2 _))
	_
	 =  HappyAbsSyn9
		 (JMP  happy_var_2
	)
happyReduction_22 _ _  = notHappyAtAll 

happyReduce_23 = happySpecReduce_2  9 happyReduction_23
happyReduction_23 (HappyTerminal (Id happy_var_2 _))
	(HappyAbsSyn13  happy_var_1)
	 =  HappyAbsSyn9
		 (J happy_var_1 happy_var_2
	)
happyReduction_23 _ _  = notHappyAtAll 

happyReduce_24 = happySpecReduce_2  9 happyReduction_24
happyReduction_24 (HappyTerminal (Id happy_var_2 _))
	_
	 =  HappyAbsSyn9
		 (CALL happy_var_2
	)
happyReduction_24 _ _  = notHappyAtAll 

happyReduce_25 = happyReduce 4 9 happyReduction_25
happyReduction_25 (_ `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_2) `HappyStk`
	_ `HappyStk`
	happyRest)
	 = HappyAbsSyn9
		 (ENTER happy_var_2
	) `HappyStk` happyRest

happyReduce_26 = happySpecReduce_1  9 happyReduction_26
happyReduction_26 _
	 =  HappyAbsSyn9
		 (LEAVE
	)

happyReduce_27 = happySpecReduce_1  9 happyReduction_27
happyReduction_27 _
	 =  HappyAbsSyn9
		 (NOP
	)

happyReduce_28 = happySpecReduce_2  10 happyReduction_28
happyReduction_28 _
	(HappyTerminal (Id happy_var_1 _))
	 =  HappyAbsSyn10
		 (happy_var_1
	)
happyReduction_28 _ _  = notHappyAtAll 

happyReduce_29 = happySpecReduce_1  11 happyReduction_29
happyReduction_29 _
	 =  HappyAbsSyn11
		 (MOV
	)

happyReduce_30 = happySpecReduce_1  11 happyReduction_30
happyReduction_30 _
	 =  HappyAbsSyn11
		 (ADD
	)

happyReduce_31 = happySpecReduce_1  11 happyReduction_31
happyReduction_31 _
	 =  HappyAbsSyn11
		 (SUB
	)

happyReduce_32 = happySpecReduce_1  11 happyReduction_32
happyReduction_32 _
	 =  HappyAbsSyn11
		 (SHL
	)

happyReduce_33 = happySpecReduce_1  11 happyReduction_33
happyReduction_33 _
	 =  HappyAbsSyn11
		 (SHR
	)

happyReduce_34 = happySpecReduce_1  11 happyReduction_34
happyReduction_34 _
	 =  HappyAbsSyn11
		 (SAL
	)

happyReduce_35 = happySpecReduce_1  11 happyReduction_35
happyReduction_35 _
	 =  HappyAbsSyn11
		 (SAR
	)

happyReduce_36 = happySpecReduce_1  11 happyReduction_36
happyReduction_36 _
	 =  HappyAbsSyn11
		 (AND
	)

happyReduce_37 = happySpecReduce_1  11 happyReduction_37
happyReduction_37 _
	 =  HappyAbsSyn11
		 (OR
	)

happyReduce_38 = happySpecReduce_1  11 happyReduction_38
happyReduction_38 _
	 =  HappyAbsSyn11
		 (XOR
	)

happyReduce_39 = happySpecReduce_1  12 happyReduction_39
happyReduction_39 _
	 =  HappyAbsSyn12
		 (POP
	)

happyReduce_40 = happySpecReduce_1  12 happyReduction_40
happyReduction_40 _
	 =  HappyAbsSyn12
		 (NEG
	)

happyReduce_41 = happySpecReduce_1  12 happyReduction_41
happyReduction_41 _
	 =  HappyAbsSyn12
		 (NOT
	)

happyReduce_42 = happySpecReduce_1  12 happyReduction_42
happyReduction_42 _
	 =  HappyAbsSyn12
		 (INC
	)

happyReduce_43 = happySpecReduce_1  12 happyReduction_43
happyReduction_43 _
	 =  HappyAbsSyn12
		 (DEC
	)

happyReduce_44 = happySpecReduce_1  13 happyReduction_44
happyReduction_44 _
	 =  HappyAbsSyn13
		 (E
	)

happyReduce_45 = happySpecReduce_1  13 happyReduction_45
happyReduction_45 _
	 =  HappyAbsSyn13
		 (NE
	)

happyReduce_46 = happySpecReduce_1  13 happyReduction_46
happyReduction_46 _
	 =  HappyAbsSyn13
		 (L
	)

happyReduce_47 = happySpecReduce_1  13 happyReduction_47
happyReduction_47 _
	 =  HappyAbsSyn13
		 (LE
	)

happyReduce_48 = happySpecReduce_1  13 happyReduction_48
happyReduction_48 _
	 =  HappyAbsSyn13
		 (G
	)

happyReduce_49 = happySpecReduce_1  13 happyReduction_49
happyReduction_49 _
	 =  HappyAbsSyn13
		 (GE
	)

happyReduce_50 = happySpecReduce_1  14 happyReduction_50
happyReduction_50 (HappyAbsSyn19  happy_var_1)
	 =  HappyAbsSyn14
		 (Imm happy_var_1
	)
happyReduction_50 _  = notHappyAtAll 

happyReduce_51 = happySpecReduce_1  14 happyReduction_51
happyReduction_51 (HappyAbsSyn15  happy_var_1)
	 =  HappyAbsSyn14
		 (Dest happy_var_1
	)
happyReduction_51 _  = notHappyAtAll 

happyReduce_52 = happySpecReduce_1  15 happyReduction_52
happyReduction_52 (HappyAbsSyn16  happy_var_1)
	 =  HappyAbsSyn15
		 (Reg happy_var_1
	)
happyReduction_52 _  = notHappyAtAll 

happyReduce_53 = happyReduce 5 15 happyReduction_53
happyReduction_53 (_ `HappyStk`
	(HappyAbsSyn17  happy_var_4) `HappyStk`
	_ `HappyStk`
	_ `HappyStk`
	_ `HappyStk`
	happyRest)
	 = HappyAbsSyn15
		 (( \ (a,b,c,d) -> Mem a b c d) happy_var_4
	) `HappyStk` happyRest

happyReduce_54 = happySpecReduce_1  16 happyReduction_54
happyReduction_54 (HappyTerminal (Tok.Reg happy_var_1 _))
	 =  HappyAbsSyn16
		 (Fixed happy_var_1
	)
happyReduction_54 _  = notHappyAtAll 

happyReduce_55 = happySpecReduce_1  16 happyReduction_55
happyReduction_55 (HappyTerminal (Tok.Temp happy_var_1 _))
	 =  HappyAbsSyn16
		 (Flex (Temp happy_var_1)
	)
happyReduction_55 _  = notHappyAtAll 

happyReduce_56 = happySpecReduce_1  17 happyReduction_56
happyReduction_56 (HappyAbsSyn16  happy_var_1)
	 =  HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing Nothing 0
	)
happyReduction_56 _  = notHappyAtAll 

happyReduce_57 = happySpecReduce_3  17 happyReduction_57
happyReduction_57 (HappyAbsSyn19  happy_var_3)
	_
	(HappyAbsSyn16  happy_var_1)
	 =  HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing Nothing happy_var_3
	)
happyReduction_57 _ _ _  = notHappyAtAll 

happyReduce_58 = happySpecReduce_3  17 happyReduction_58
happyReduction_58 (HappyAbsSyn16  happy_var_3)
	_
	(HappyAbsSyn16  happy_var_1)
	 =  HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing (Just happy_var_3) 0
	)
happyReduction_58 _ _ _  = notHappyAtAll 

happyReduce_59 = happySpecReduce_3  17 happyReduction_59
happyReduction_59 (HappyAbsSyn16  happy_var_3)
	_
	(HappyAbsSyn19  happy_var_1)
	 =  HappyAbsSyn17
		 ((,,,) happy_var_3 Nothing Nothing happy_var_1
	)
happyReduction_59 _ _ _  = notHappyAtAll 

happyReduce_60 = happyReduce 5 17 happyReduction_60
happyReduction_60 ((HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing (Just happy_var_3) happy_var_5
	) `HappyStk` happyRest

happyReduce_61 = happyReduce 5 17 happyReduction_61
happyReduction_61 ((HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing (Just happy_var_5) happy_var_3
	) `HappyStk` happyRest

happyReduce_62 = happyReduce 5 17 happyReduction_62
happyReduction_62 ((HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 Nothing (Just happy_var_5) happy_var_1
	) `HappyStk` happyRest

happyReduce_63 = happySpecReduce_3  17 happyReduction_63
happyReduction_63 (HappyAbsSyn18  happy_var_3)
	_
	(HappyAbsSyn16  happy_var_1)
	 =  HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) Nothing 0
	)
happyReduction_63 _ _ _  = notHappyAtAll 

happyReduce_64 = happySpecReduce_3  17 happyReduction_64
happyReduction_64 (HappyAbsSyn16  happy_var_3)
	_
	(HappyAbsSyn18  happy_var_1)
	 =  HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) Nothing 0
	)
happyReduction_64 _ _ _  = notHappyAtAll 

happyReduce_65 = happyReduce 5 17 happyReduction_65
happyReduction_65 ((HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) Nothing happy_var_5
	) `HappyStk` happyRest

happyReduce_66 = happyReduce 5 17 happyReduction_66
happyReduction_66 ((HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) Nothing happy_var_5
	) `HappyStk` happyRest

happyReduce_67 = happyReduce 5 17 happyReduction_67
happyReduction_67 ((HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) (Just happy_var_5) 0
	) `HappyStk` happyRest

happyReduce_68 = happyReduce 5 17 happyReduction_68
happyReduction_68 ((HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) (Just happy_var_5) 0
	) `HappyStk` happyRest

happyReduce_69 = happyReduce 5 17 happyReduction_69
happyReduction_69 ((HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_5) Nothing happy_var_1
	) `HappyStk` happyRest

happyReduce_70 = happyReduce 5 17 happyReduction_70
happyReduction_70 ((HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_3) Nothing happy_var_1
	) `HappyStk` happyRest

happyReduce_71 = happyReduce 5 17 happyReduction_71
happyReduction_71 ((HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_5) (Just happy_var_1) 0
	) `HappyStk` happyRest

happyReduce_72 = happyReduce 5 17 happyReduction_72
happyReduction_72 ((HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_3) (Just happy_var_1) 0
	) `HappyStk` happyRest

happyReduce_73 = happyReduce 7 17 happyReduction_73
happyReduction_73 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) (Just happy_var_7) happy_var_5
	) `HappyStk` happyRest

happyReduce_74 = happyReduce 7 17 happyReduction_74
happyReduction_74 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) (Just happy_var_7) happy_var_5
	) `HappyStk` happyRest

happyReduce_75 = happyReduce 7 17 happyReduction_75
happyReduction_75 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) (Just happy_var_5) happy_var_7
	) `HappyStk` happyRest

happyReduce_76 = happyReduce 7 17 happyReduction_76
happyReduction_76 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) (Just happy_var_5) happy_var_7
	) `HappyStk` happyRest

happyReduce_77 = happyReduce 7 17 happyReduction_77
happyReduction_77 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_5) (Just happy_var_7) happy_var_1
	) `HappyStk` happyRest

happyReduce_78 = happyReduce 7 17 happyReduction_78
happyReduction_78 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_3) (Just happy_var_7) happy_var_1
	) `HappyStk` happyRest

happyReduce_79 = happyReduce 7 17 happyReduction_79
happyReduction_79 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_5) (Just happy_var_1) happy_var_7
	) `HappyStk` happyRest

happyReduce_80 = happyReduce 7 17 happyReduction_80
happyReduction_80 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_3) (Just happy_var_1) happy_var_7
	) `HappyStk` happyRest

happyReduce_81 = happyReduce 7 17 happyReduction_81
happyReduction_81 ((HappyAbsSyn18  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_7) (Just happy_var_1) happy_var_3
	) `HappyStk` happyRest

happyReduce_82 = happyReduce 7 17 happyReduction_82
happyReduction_82 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_7 (Just happy_var_5) (Just happy_var_1) happy_var_3
	) `HappyStk` happyRest

happyReduce_83 = happyReduce 7 17 happyReduction_83
happyReduction_83 ((HappyAbsSyn18  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_7) (Just happy_var_3) happy_var_1
	) `HappyStk` happyRest

happyReduce_84 = happyReduce 7 17 happyReduction_84
happyReduction_84 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_7 (Just happy_var_5) (Just happy_var_3) happy_var_1
	) `HappyStk` happyRest

happyReduce_85 = happySpecReduce_3  17 happyReduction_85
happyReduction_85 (HappyAbsSyn19  happy_var_3)
	_
	(HappyAbsSyn16  happy_var_1)
	 =  HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing Nothing (- happy_var_3)
	)
happyReduction_85 _ _ _  = notHappyAtAll 

happyReduce_86 = happyReduce 5 17 happyReduction_86
happyReduction_86 ((HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing (Just happy_var_3) (- happy_var_5)
	) `HappyStk` happyRest

happyReduce_87 = happyReduce 5 17 happyReduction_87
happyReduction_87 ((HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 Nothing (Just happy_var_5) (- happy_var_3)
	) `HappyStk` happyRest

happyReduce_88 = happyReduce 5 17 happyReduction_88
happyReduction_88 ((HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) Nothing (- happy_var_5)
	) `HappyStk` happyRest

happyReduce_89 = happyReduce 5 17 happyReduction_89
happyReduction_89 ((HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) Nothing (- happy_var_5)
	) `HappyStk` happyRest

happyReduce_90 = happyReduce 7 17 happyReduction_90
happyReduction_90 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) (Just happy_var_7) (- happy_var_5)
	) `HappyStk` happyRest

happyReduce_91 = happyReduce 7 17 happyReduction_91
happyReduction_91 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) (Just happy_var_7) (- happy_var_5)
	) `HappyStk` happyRest

happyReduce_92 = happyReduce 7 17 happyReduction_92
happyReduction_92 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_1 (Just happy_var_3) (Just happy_var_5) (- happy_var_7)
	) `HappyStk` happyRest

happyReduce_93 = happyReduce 7 17 happyReduction_93
happyReduction_93 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_1) (Just happy_var_5) (- happy_var_7)
	) `HappyStk` happyRest

happyReduce_94 = happyReduce 7 17 happyReduction_94
happyReduction_94 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_3 (Just happy_var_5) (Just happy_var_1) (- happy_var_7)
	) `HappyStk` happyRest

happyReduce_95 = happyReduce 7 17 happyReduction_95
happyReduction_95 ((HappyAbsSyn19  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_3) (Just happy_var_1) (- happy_var_7)
	) `HappyStk` happyRest

happyReduce_96 = happyReduce 7 17 happyReduction_96
happyReduction_96 ((HappyAbsSyn18  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_5 (Just happy_var_7) (Just happy_var_1) (- happy_var_3)
	) `HappyStk` happyRest

happyReduce_97 = happyReduce 7 17 happyReduction_97
happyReduction_97 ((HappyAbsSyn16  happy_var_7) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn18  happy_var_5) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn19  happy_var_3) `HappyStk`
	_ `HappyStk`
	(HappyAbsSyn16  happy_var_1) `HappyStk`
	happyRest)
	 = HappyAbsSyn17
		 ((,,,) happy_var_7 (Just happy_var_5) (Just happy_var_1) (- happy_var_3)
	) `HappyStk` happyRest

happyReduce_98 = happySpecReduce_1  18 happyReduction_98
happyReduction_98 _
	 =  HappyAbsSyn18
		 (S2
	)

happyReduce_99 = happySpecReduce_1  18 happyReduction_99
happyReduction_99 _
	 =  HappyAbsSyn18
		 (S4
	)

happyReduce_100 = happySpecReduce_1  18 happyReduction_100
happyReduction_100 _
	 =  HappyAbsSyn18
		 (S8
	)

happyReduce_101 = happySpecReduce_1  19 happyReduction_101
happyReduction_101 _
	 =  HappyAbsSyn19
		 (fromIntegral 0
	)

happyReduce_102 = happySpecReduce_1  19 happyReduction_102
happyReduction_102 _
	 =  HappyAbsSyn19
		 (fromIntegral 2
	)

happyReduce_103 = happySpecReduce_1  19 happyReduction_103
happyReduction_103 _
	 =  HappyAbsSyn19
		 (fromIntegral 4
	)

happyReduce_104 = happySpecReduce_1  19 happyReduction_104
happyReduction_104 _
	 =  HappyAbsSyn19
		 (fromIntegral 8
	)

happyReduce_105 = happySpecReduce_1  19 happyReduction_105
happyReduction_105 (HappyTerminal (Nat happy_var_1 _))
	 =  HappyAbsSyn19
		 (fromIntegral happy_var_1
	)
happyReduction_105 _  = notHappyAtAll 

happyReduce_106 = happySpecReduce_1  20 happyReduction_106
happyReduction_106 (HappyAbsSyn19  happy_var_1)
	 =  HappyAbsSyn19
		 (happy_var_1
	)
happyReduction_106 _  = notHappyAtAll 

happyReduce_107 = happySpecReduce_2  20 happyReduction_107
happyReduction_107 (HappyAbsSyn19  happy_var_2)
	_
	 =  HappyAbsSyn19
		 ((- happy_var_2)
	)
happyReduction_107 _ _  = notHappyAtAll 

happyNewToken action sts stk [] =
	action 74 74 notHappyAtAll (HappyState action) sts stk []

happyNewToken action sts stk (tk:tks) =
	let cont i = action i i tk (HappyState action) sts stk tks in
	case tk of {
	LBrack _ -> cont 21;
	RBrack _ -> cont 22;
	Colon _ -> cont 23;
	Comma _ -> cont 24;
	Dot _ -> cont 25;
	Plus _ -> cont 26;
	Minus _ -> cont 27;
	Times _ -> cont 28;
	Nat 0 _ -> cont 29;
	Nat 2 _ -> cont 30;
	Nat 4 _ -> cont 31;
	Nat 8 _ -> cont 32;
	Args _ -> cont 33;
	LOC _ -> cont 34;
	REG _ -> cont 35;
	Mov _ -> cont 36;
	Add _ -> cont 37;
	Sub _ -> cont 38;
	Shl _ -> cont 39;
	Shr _ -> cont 40;
	Sal _ -> cont 41;
	Sar _ -> cont 42;
	And _ -> cont 43;
	Or _ -> cont 44;
	Xor _ -> cont 45;
	Neg _ -> cont 46;
	Not _ -> cont 47;
	Inc _ -> cont 48;
	Dec _ -> cont 49;
	Pop _ -> cont 50;
	Push _ -> cont 51;
	Imul _ -> cont 52;
	Idiv _ -> cont 53;
	Jmp _ -> cont 54;
	Lea _ -> cont 55;
	Cmp _ -> cont 56;
	Je _ -> cont 57;
	Jne _ -> cont 58;
	Jl _ -> cont 59;
	Jle _ -> cont 60;
	Jg _ -> cont 61;
	Jge _ -> cont 62;
	Call _ -> cont 63;
	Ret _ -> cont 64;
	Enter _ -> cont 65;
	Leave _ -> cont 66;
	Nop _ -> cont 67;
	Dword _ -> cont 68;
	Ptr _ -> cont 69;
	Nat happy_dollar_dollar _ -> cont 70;
	Tok.Temp happy_dollar_dollar _ -> cont 71;
	Tok.Reg happy_dollar_dollar _ -> cont 72;
	Id happy_dollar_dollar _ -> cont 73;
	_ -> happyError' (tk:tks)
	}

happyError_ 74 tk tks = happyError' tks
happyError_ _ tk tks = happyError' (tk:tks)

newtype HappyIdentity a = HappyIdentity a
happyIdentity = HappyIdentity
happyRunIdentity (HappyIdentity a) = a

instance Monad HappyIdentity where
    return = HappyIdentity
    (HappyIdentity p) >>= q = q p

happyThen :: () => HappyIdentity a -> (a -> HappyIdentity b) -> HappyIdentity b
happyThen = (>>=)
happyReturn :: () => a -> HappyIdentity a
happyReturn = (return)
happyThen1 m k tks = (>>=) m (\a -> k a tks)
happyReturn1 :: () => a -> b -> HappyIdentity a
happyReturn1 = \a tks -> (return) a
happyError' :: () => [((Token AlexPosn))] -> HappyIdentity a
happyError' = HappyIdentity . happyError

parse tks = happyRunIdentity happySomeParser where
  happySomeParser = happyThen (happyParse action_0 tks) (\x -> case x of {HappyAbsSyn4 z -> happyReturn z; _other -> notHappyAtAll })

happySeq = happyDontSeq


happyError :: [Token AlexPosn] -> a
happyError tks = error ("Parse error at " ++ lcn ++ "\n")
	where
	lcn = 	case tks of
		  [] -> "end of file"
		  (tk:_) -> "line " ++ show l ++ ", column " ++ show c ++ " (token " ++ filterPn (show tk) ++ ")"
			where AlexPn _ l c = token_pos tk
{-# LINE 1 "templates/GenericTemplate.hs" #-}
{-# LINE 1 "templates/GenericTemplate.hs" #-}
{-# LINE 1 "<built-in>" #-}
{-# LINE 1 "<command-line>" #-}
{-# LINE 1 "templates/GenericTemplate.hs" #-}
-- Id: GenericTemplate.hs,v 1.26 2005/01/14 14:47:22 simonmar Exp 

{-# LINE 30 "templates/GenericTemplate.hs" #-}








{-# LINE 51 "templates/GenericTemplate.hs" #-}

{-# LINE 61 "templates/GenericTemplate.hs" #-}

{-# LINE 70 "templates/GenericTemplate.hs" #-}

infixr 9 `HappyStk`
data HappyStk a = HappyStk a (HappyStk a)

-----------------------------------------------------------------------------
-- starting the parse

happyParse start_state = happyNewToken start_state notHappyAtAll notHappyAtAll

-----------------------------------------------------------------------------
-- Accepting the parse

-- If the current token is (1), it means we've just accepted a partial
-- parse (a %partial parser).  We must ignore the saved token on the top of
-- the stack in this case.
happyAccept (1) tk st sts (_ `HappyStk` ans `HappyStk` _) =
	happyReturn1 ans
happyAccept j tk st sts (HappyStk ans _) = 
	 (happyReturn1 ans)

-----------------------------------------------------------------------------
-- Arrays only: do the next action

{-# LINE 148 "templates/GenericTemplate.hs" #-}

-----------------------------------------------------------------------------
-- HappyState data type (not arrays)



newtype HappyState b c = HappyState
        (Int ->                    -- token number
         Int ->                    -- token number (yes, again)
         b ->                           -- token semantic value
         HappyState b c ->              -- current state
         [HappyState b c] ->            -- state stack
         c)



-----------------------------------------------------------------------------
-- Shifting a token

happyShift new_state (1) tk st sts stk@(x `HappyStk` _) =
     let (i) = (case x of { HappyErrorToken (i) -> i }) in
--     trace "shifting the error token" $
     new_state i i tk (HappyState (new_state)) ((st):(sts)) (stk)

happyShift new_state i tk st sts stk =
     happyNewToken new_state ((st):(sts)) ((HappyTerminal (tk))`HappyStk`stk)

-- happyReduce is specialised for the common cases.

happySpecReduce_0 i fn (1) tk st sts stk
     = happyFail (1) tk st sts stk
happySpecReduce_0 nt fn j tk st@((HappyState (action))) sts stk
     = action nt j tk st ((st):(sts)) (fn `HappyStk` stk)

happySpecReduce_1 i fn (1) tk st sts stk
     = happyFail (1) tk st sts stk
happySpecReduce_1 nt fn j tk _ sts@(((st@(HappyState (action))):(_))) (v1`HappyStk`stk')
     = let r = fn v1 in
       happySeq r (action nt j tk st sts (r `HappyStk` stk'))

happySpecReduce_2 i fn (1) tk st sts stk
     = happyFail (1) tk st sts stk
happySpecReduce_2 nt fn j tk _ ((_):(sts@(((st@(HappyState (action))):(_))))) (v1`HappyStk`v2`HappyStk`stk')
     = let r = fn v1 v2 in
       happySeq r (action nt j tk st sts (r `HappyStk` stk'))

happySpecReduce_3 i fn (1) tk st sts stk
     = happyFail (1) tk st sts stk
happySpecReduce_3 nt fn j tk _ ((_):(((_):(sts@(((st@(HappyState (action))):(_))))))) (v1`HappyStk`v2`HappyStk`v3`HappyStk`stk')
     = let r = fn v1 v2 v3 in
       happySeq r (action nt j tk st sts (r `HappyStk` stk'))

happyReduce k i fn (1) tk st sts stk
     = happyFail (1) tk st sts stk
happyReduce k nt fn j tk st sts stk
     = case happyDrop (k - ((1) :: Int)) sts of
	 sts1@(((st1@(HappyState (action))):(_))) ->
        	let r = fn stk in  -- it doesn't hurt to always seq here...
       		happyDoSeq r (action nt j tk st1 sts1 r)

happyMonadReduce k nt fn (1) tk st sts stk
     = happyFail (1) tk st sts stk
happyMonadReduce k nt fn j tk st sts stk =
        happyThen1 (fn stk tk) (\r -> action nt j tk st1 sts1 (r `HappyStk` drop_stk))
       where (sts1@(((st1@(HappyState (action))):(_)))) = happyDrop k ((st):(sts))
             drop_stk = happyDropStk k stk

happyMonad2Reduce k nt fn (1) tk st sts stk
     = happyFail (1) tk st sts stk
happyMonad2Reduce k nt fn j tk st sts stk =
       happyThen1 (fn stk tk) (\r -> happyNewToken new_state sts1 (r `HappyStk` drop_stk))
       where (sts1@(((st1@(HappyState (action))):(_)))) = happyDrop k ((st):(sts))
             drop_stk = happyDropStk k stk





             new_state = action


happyDrop (0) l = l
happyDrop n ((_):(t)) = happyDrop (n - ((1) :: Int)) t

happyDropStk (0) l = l
happyDropStk n (x `HappyStk` xs) = happyDropStk (n - ((1)::Int)) xs

-----------------------------------------------------------------------------
-- Moving to a new state after a reduction

{-# LINE 246 "templates/GenericTemplate.hs" #-}
happyGoto action j tk st = action j j tk (HappyState action)


-----------------------------------------------------------------------------
-- Error recovery ((1) is the error token)

-- parse error if we are in recovery and we fail again
happyFail (1) tk old_st _ stk@(x `HappyStk` _) =
     let (i) = (case x of { HappyErrorToken (i) -> i }) in
--	trace "failing" $ 
        happyError_ i tk

{-  We don't need state discarding for our restricted implementation of
    "error".  In fact, it can cause some bogus parses, so I've disabled it
    for now --SDM

-- discard a state
happyFail  (1) tk old_st (((HappyState (action))):(sts)) 
						(saved_tok `HappyStk` _ `HappyStk` stk) =
--	trace ("discarding state, depth " ++ show (length stk))  $
	action (1) (1) tk (HappyState (action)) sts ((saved_tok`HappyStk`stk))
-}

-- Enter error recovery: generate an error token,
--                       save the old token and carry on.
happyFail  i tk (HappyState (action)) sts stk =
--      trace "entering error recovery" $
	action (1) (1) tk (HappyState (action)) sts ( (HappyErrorToken (i)) `HappyStk` stk)

-- Internal happy errors:

notHappyAtAll :: a
notHappyAtAll = error "Internal Happy error\n"

-----------------------------------------------------------------------------
-- Hack to get the typechecker to accept our action functions







-----------------------------------------------------------------------------
-- Seq-ing.  If the --strict flag is given, then Happy emits 
--	happySeq = happyDoSeq
-- otherwise it emits
-- 	happySeq = happyDontSeq

happyDoSeq, happyDontSeq :: a -> b -> b
happyDoSeq   a b = a `seq` b
happyDontSeq a b = b

-----------------------------------------------------------------------------
-- Don't inline any functions from the template.  GHC has a nasty habit
-- of deciding to inline happyGoto everywhere, which increases the size of
-- the generated parser quite a bit.

{-# LINE 312 "templates/GenericTemplate.hs" #-}
{-# NOINLINE happyShift #-}
{-# NOINLINE happySpecReduce_0 #-}
{-# NOINLINE happySpecReduce_1 #-}
{-# NOINLINE happySpecReduce_2 #-}
{-# NOINLINE happySpecReduce_3 #-}
{-# NOINLINE happyReduce #-}
{-# NOINLINE happyMonadReduce #-}
{-# NOINLINE happyGoto #-}
{-# NOINLINE happyFail #-}

-- end of Happy Template.

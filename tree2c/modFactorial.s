.intel_syntax noprefix
.globl Lmain

Lmain:
  push ebp
  mov ebp, esp
  push 10

  call Lfact
  add esp, 4
  push eax
  call L_println_int
  add esp, 4
  mov eax, 0
  mov esp, ebp
  pop ebp
  ret


Lfact:
  push ebp
  mov ebp, esp
  push edi
  push esi

  mov edi, [ebp+8]
  mov esi, edi
  cmp edi, 0
  je Lbase

  sub esi, 1
  push esi
  call Lfact
  add esp, 4
  imul eax, edi
  jmp Lend


Lbase:
  mov eax, 1

Lend:
  pop esi
  pop edi
  mov esp, ebp
  pop ebp
  ret

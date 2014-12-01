.intel_syntax noprefix
.globl Lmain
.type Lmain, @function

Lmain:
  # prologue
  push ebp
  mov ebp, esp
  push ebx
  push edi
  push esi
  
  push 10
  call Lfact
  add esp, 4
  
  push eax
  call L_println_int
  add esp, 4
  
  mov eax, 0
  
  # epilogue
  pop esi
  pop edi
  pop ebx
  mov esp, ebp
  pop ebp
  ret


Lfact:
  # prologue
  push ebp
  mov ebp, esp
  push ebx
  push edi
  push esi

  mov edi, [ebp+8] # get first parameter and save in edi (for imul with return val in eax of next iteration)
  mov esi, edi
  cmp edi, 0
  je Lbase
  
  # recursive call
  sub esi, 1
  push esi
  call Lfact
  add esp, 4#  remove space for parameter
  imul eax, edi
  jmp Lend


Lbase:
  mov eax, 1

Lend:
  # epilogue
  pop esi
  pop edi
  pop ebx
  mov esp, ebp
  pop ebp
  ret

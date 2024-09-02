global main
extern printf

section .data

	message:	db "Hello World", 10, 0
	
section .text

main:
	push	ebp
	mov		ebp, esp
	
	push	dword	message
	call	printf
	add		esp, 4
	
	mov		esp, ebp
	pop		ebp
	ret
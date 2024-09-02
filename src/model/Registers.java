package model;

import java.util.*;

public class Registers {

	private HashSet<String> registers;

	String _registers = "ah,al,ax,eax,rax,bh,bl,bx,ebx,rbx,ch,cl,cx,ecx,rcx,dh,dl,dx,edx,rdx,dil,di,edi,rdi,sil,si,esi,rsi,bpl,bp,ebp,rbp,sp,rsp";
			
	public Registers() {
		
		registers = new HashSet<String>();
		
		String array[] = _registers.split(",");
		for (String s : array) {
			registers.add( s );
		}
		for (int i = 8; i < 16; ++i) {
			registers.add( "r" + i + "l" );
			registers.add( "r" + i );
		}
		for (int i = 0; i < 8; ++i) {
			registers.add( "st" + i );
			registers.add( "mmx" + i );
		}
		for (int i= 0; i < 16; ++i) {
			registers.add( "xmm" + i );
			registers.add( "ymm" + i );
		}
		for (int i= 0; i < 32; ++i) {
			registers.add( "zmm" + i );
		}
	}
	
	public boolean contains( String s ) {
		return registers.contains( s );
	}
	
}
simdest=$(HOME)/public_html

GHC = ghc
GHC_OPTS = -ignore-package monads-fd
# -fglasgow-exts 

ghc=$(GHC) $(GHC_OPTS)

simout   = risc386
simstems = Util GenSym Frame Intel FrameIntel TokenIntel LexIntel ParseIntel StateIntel MainIntel
simsrcs  = $(foreach stem,$(simstems),$(stem).hs)
simpack  = risc386.cabal Makefile LIESMICH.html README.txt LICENSE LexIntel.x ParseIntel.y beispiel.s $(simsrcs) $(simout)

.PHONY : dist default sim simship test clean veryclean

default : risc386

sim : $(simout)

$(simout) : $(simsrcs)
	$(ghc) --make -o $@ MainIntel.hs

dist : $(simpack)
	cabal sdist
	cp -p dist/risc386*.tar.gz $(simdest)

simship : sim.tar.gz
	cp -p $^ $(simdest)

sim.tar.gz : $(simpack)
	tar czf $@ $^

%.hs:	%.y
	happy -iGRM.LOG $<

%.hs:	%.x
	alex $<

%.o:	%.hs
	$(GHC) $(GHC_OPTS) -c -o $@ $<

test:
	risc386 beispiel.s 2> /dev/null
	risc386 beispiel_gross.s 2> /dev/null

clean:
	-rm *.hi *.o risc386

veryclean:
	-rm *.hi *.o MJLex.hs MJParse.hs risc386

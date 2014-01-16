SOURCES=CBot.$(EXT)

ifeq ($(EXT), c)
    CC=gcc
else
    CC=g++
endif    

CFLAGS=-O2 -fno-strict-aliasing -fPIC -fno-omit-frame-pointer -W -Wall -Wno-unused -Wno-parentheses -I.
OBJECTS=$(SOURCES:%.$(EXT)=%.o)
LIBRARY=libCBot.so
LDFLAGS=-Wl,-soname=$(LIBRARY) -static-libgcc -mimpure-text
LIBRARIES=Bot.o -lc
LINK_SHARED=$(LINK.$(EXT)) -shared -o $@
CFLAGS += -I$(JDK)/include -I$(JDK)/include/linux -m$(BIT)

all: $(LIBRARY)

$(LIBRARY): $(OBJECTS)
	$(LINK_SHARED) $(OBJECTS) $(LIBRARIES)

clean:
	rm -f $(LIBRARY) $(OBJECTS)

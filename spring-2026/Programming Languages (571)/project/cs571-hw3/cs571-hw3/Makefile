CC = gcc
CFLAGS = -Wall -Wextra -O2

TARGET = arith
SRC = arith.c
ZIP = hw3.zip

.PHONY: all grade clean submit

all: $(TARGET)

$(TARGET): $(SRC)
	$(CC) $(CFLAGS) -o $(TARGET) $(SRC)

grade: $(TARGET)
	python grader.py ./$(TARGET)
# 	python3 grader.py ./$(TARGET)
submit:
	powershell Compress-Archive -Force $(SRC) $(ZIP)
# 	zip $(ZIP) $(SRC)

clean:
	del /Q arith.exe hw3.zip
# 	rm -f $(TARGET) $(ZIP)
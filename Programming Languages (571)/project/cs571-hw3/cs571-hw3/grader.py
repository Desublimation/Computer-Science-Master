import subprocess
import sys

TIMEOUT = 5

test_cases = [
    {
        "name": "Test 1: associativity",
        "input": "2^3^2\n",
        "expected_output": "512",
        "points": 10
    },
    {
        "name": "Test 2: precedence",
        "input": "2*3^2\n",
        "expected_output": "18",
        "points": 10
    },
    {
        "name": "Test 3: parentheses",
        "input": "(2^3)^(2)\n",
        "expected_output": "64",
        "points": 10
    },
    {
        "name": "Test 4: mixed operations",
        "input": "(1+2)*3^(1+1)*2+5\n",
        "expected_output": "59",
        "points": 10
    }
]


def extract_last_word(text):
    words = text.strip().split()
    if not words:
        return ""
    return words[-1]


def run_test(binary, test):
    try:
        process = subprocess.Popen(
            [binary],
            stdin=subprocess.PIPE,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )

        stdout, stderr = process.communicate(
            input=test["input"],
            timeout=TIMEOUT
        )

        output_word = extract_last_word(stdout)
        expected = test["expected_output"].strip()

        if output_word == expected:
            return True, output_word
        else:
            return False, output_word

    except subprocess.TimeoutExpired:
        process.kill()
        return False, "TIMEOUT"


def grade(binary):
    total_score = 0
    max_score = 0

    for test in test_cases:
        max_score += test["points"]

        passed, output = run_test(binary, test)

        if passed:
            total_score += test["points"]
            print(f"{test['name']}: PASS ({test['points']} pts)")
        else:
            print(f"{test['name']}: FAIL")
            print(f"Expected: {test['expected_output']}")
            print(f"Got: {output}")

    print("\nFinal Score:", total_score, "/", max_score)

def main():
    if len(sys.argv) != 2:
        print("Usage: python autograder.py <binary>")
        sys.exit(1)

    binary = sys.argv[1]
    grade(binary)

if __name__ == "__main__":
    main()
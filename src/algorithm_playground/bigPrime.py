def largest_primes_under(number, cap):
    n = cap - 1
    while number and n >= 2:
        if all(n % d for d in range(2, int(n ** 0.5 + 1))):
            yield n
            number -= 1
        n -= 1

for p in largest_primes_under(10, 2147483647):
    print(p)
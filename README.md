Artificial City
===============

Tematem naszego projektu jest symulacja ruchu drogowego w warunkach miejskich. Naszym celem było zaprojektowanie i zaimplementowanie automatu komórkowego, który wiernie oddawałby realia poruszania się w ośrodku miejskim. Taki program mógbły pomóc w projektowaniu i testowaniu bezpiecznych dróg i skrzyżowań w miastach w całej Polsce.

Nasz model uwzględnia nie tylko użytkowników aut jako uczestników ruchu drogowego, ale także
i pieszych. Obecność pieszych i skrzyżowań narzuciło nam dodanie zupełnie nowych elementów,
których nie ma w prostszych modelach, takie jak przejścia dla pieszych albo sygnalizacja świetlna.

Poruszanie się pojazdów
------
Pojady poruszają się według teoretycznego modelu ruchu drogowego: [Nagel–Schreckenberg model](https://en.wikipedia.org/wiki/Nagel%E2%80%93Schreckenberg_model)

Sygnalizacja Świetlna
---
Dla uproszczenia pomijamy żółte światło. Gdy światło jest zielone, samochody mogą ruszać, gdy jest czerwone, oczywiście powinniśmy się zatrzymać. Każde światła kontrolują konkretne komórki na drodze, które puszczają samochody lub nie. Skrzyżowanie jest sterowane kontrolerem, do którego można wprowadzić dowolną konfigurację w jaki sposób i po jakim czasie będą zmieniać się kolory świateł.

![](NowyKleparzMapa.png)

Skręcanie Pojazdów
---
Każde z przejezdnych pól jest opisane przez macierz reprezentującą szansę na skręt w danym
kierunku przez auto przyjeżdżające z danego kierunku. Modyfikowalnym atrybutem każdego przejezdnego pola jest również maksymalna prędkość, z jaką można się na niej poruszać.

Ruch pieszych
---
Piesi poruszają się dzięki zastosowaniu pól statycznych w połączeniu z [sąsiedztwem Moore'a](https://en.wikipedia.org/wiki/Moore_neighborhood), znanych z takich modelów agentowych jak np. Crowd Dynamics.
Na przejściach dla pieszych zachowane jest bezwględne pierszeństwo pieszych, co pozwala ich użytkownikom na w pełni bezpieczne poruszanie się na drugą stronę ulicy.

Przykładowa symulacja ruchu na Nowym Kleparzu w Krakowie:
---
![](NowyKleparzSymulacja.png)

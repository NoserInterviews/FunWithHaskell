# FunWithHaskell

Ein Projekt, das anhand eines Schedulers aufzeigt, wie einige Konzepte von Haskell in Java/Kotlin umgesetzt werden können.

Die verschiedenen Ausbaustufen des Schedulers befinden sich in den verschiedenen Branches feature/schrittX.

Schritt3 ist schon der ganze Scheduling Algorithmus. In Schritt4 finden sich noch zusätzliche Features (unter anderem ein funktionaler Cache)


- Immutable Objects: Task
- Sum types: Seq, Maybe, Try
- Persistent collections (collections, die immutable sind): Seq
- Rekursion mit tail-call Optimierung: Seq
- Abstraktion von Konzepten:
    - Iteration: Seq
    - Fehlende Werte: Maybe
    - Operationen die schief gehen können: Try



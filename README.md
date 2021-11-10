# How to Run
1. If not compiled, compile the program using `javac join.java`
2. Run `java join [input1.tbl] [join_col_name1] [input2.tbl] [join_col_name2] [NESTED_LOOP / HASH] [output.tbl] [NORMAL / BENCHMARK]` 
    - `[input1.tbl]` - File name for left side of join
    - `[join_col_name1]` - Name of the left join column
    - `[input2.tbl]` - File name for left side of join
    - `[join_col_name2]` - Name of the right join column
    - `[NESTED_LOOP / HASH]` - Use `NESTED_LOOP` for nested loop join, `HASH` for hash join
    - `[output.tbl]` - File name for output
    - `[NORMAL / BENCHMARK]` - BENCHMARK mode automatically reduces data size (for faster runs)

#### Sample Run (Join Nation / Region)
`java join nation.tbl REGIONKEY region.tbl REGIONKEY NESTED_LOOP output.tbl NORMAL`

# Benchmark Result

## Nested Loop Join
`java join lineitem.tbl ORDERKEY orders.tbl ORDERKEY NESTED_LOOP output2.tbl BENCHMARK`

Time taken (in microseconds)

1. 267769972
2. 275407645
3. 276796631
4. 275719284
5. 269810028
6. 271818524
7. 276730875
8. 273292837
9. 275462820
9. 275049177

Average: 273785779 microseconds

## Hash Join
`java join lineitem.tbl ORDERKEY orders.tbl ORDERKEY HASH output2.tbl BENCHMARK`

Time taken (in microseconds)

1. 403637
2. 397868
3. 413947
4. 416700
5. 446172
6. 482340
7. 433316
8. 436812
9. 437378
10. 451203

Average: 431937 microseconds

**Benchmark Result: Hash Join is approximately 634x faster than Nest Loop Join**
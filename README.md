# How to Run
1. If not compiled, compile the program using `javac join.java`
2. Run `java join [input1.tbl] [join_col_name1] [input2.tbl] [join_col_name2] [NESTED_LOOP / HASH] [output.tbl]` 
    - `[input1.tbl]` - File name for left side of join
    - `[join_col_name1]` - Name of the left join column
    - `[input2.tbl]` - File name for left side of join
    - `[join_col_name2]` - Name of the right join column
    - `[NESTED_LOOP / HASH]` - Use `NESTED_LOOP` for nested loop join, `HASH` for hash join
    - `[output.tbl]` - File name for output

#### Sample Run (Join Nation / Region)
`java join nation.tbl REGIONKEY region.tbl REGIONKEY NESTED_LOOP output.tbl`

# Benchmark Result

## Nested Loop Join
`java join lineitem.tbl ORDERKEY orders.tbl ORDERKEY NESTED_LOOP output2.tbl`

Time taken (in microseconds)

1. 

## Hash Join
`java join lineitem.tbl ORDERKEY orders.tbl ORDERKEY HASH output2.tbl`

Time taken (in microseconds)

1. 
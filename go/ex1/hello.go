package main


import ( 
	"fmt"
	"io"
	"math"
	"os"
	"strings"
	"golang.org/x/tour/pic"
	"golang.org/x/tour/reader"
	"golang.org/x/tour/wc"
)

func showGame() {
	// Create a tic-tac-toe board.
	game := [][]string{
		[]string{"_", "_", "_"},
		[]string{"_", "_", "_"},
		[]string{"_", "_", "_"},
	}

	// The players take turns.
	game[0][0] = "X"
	game[2][2] = "O"
	game[2][0] = "X"
	game[1][0] = "O"
	game[0][2] = "X"

	printBoard(game)
}

func printBoard(s [][]string) {
	for i := 0; i < len(s); i++ {
		fmt.Printf("%s\n", strings.Join(s[i], " "))
	}
}

type Red struct {
	X int
	Y int
	name string
}

func (r *Red) Name() string {
	return r.name
}

func createRed() {
    paul := new(Red)
    paul.X = 5
    paul.Y = 6
    paul.name = "Paul Webster"
    fmt.Println(paul.Name())
}

func showSlice() {
	a := make([]int, 5)
	printSlice("a", a)
	b := make([]int, 0, 5)
	printSlice("b", b)
	c := b[:2]
	printSlice("c", c)
	d := c[2:5]
	printSlice("d", d)
}

func printSlice(s string, x []int) {
	fmt.Printf("%s len=%d cap=%d %v\n",
		s, len(x), cap(x), x)
}

func appendSlice() {
	var a []int
	printSlice("a", a)

	// append works on nil slices.
	a = append(a, 0)
	printSlice("a", a)

	// the slice grows as needed.
	a = append(a, 1)
	printSlice("a", a)

	// we can add more than one element at a time.
	a = append(a, 2, 3, 4)
	printSlice("a", a)
}

func showRange() {
	var pow = []int{1, 2, 4, 8, 16, 32, 64, 128}
	for i, v := range pow {
		fmt.Printf("2**%d = %d\n", i, v)
	}
}

func showRange2() {
	pow := make([]int, 10)
	for i := range pow {
		pow[i] = 1 << uint(i)
	}
	for _, value := range pow {
		fmt.Printf("%d\n", value)
	}
}

func Pic(dx, dy int) [][]uint8 {
	rs := make([][]uint8, dy)
	for i := range rs {
		rs[i] = make([]uint8, dx)
		for j := range rs[i] {
			rs[i][j] = uint8(i)*uint8(j)
		}
	}
	return rs
}

func showPic() {
	pic.Show(Pic)
}

func showMap() {
	m := make(map[string]string)
	m["Webster"] = "Paul Webster"
	m["Gheorge"] = "Bogdan Gheorge"
	fmt.Println(m["Webster"])
}

func showMap2() {
	m := map[string]string {
		"Webster": "Paul Webster",
		"Gheorge": "Bogdan Gheorge",
	}
	fmt.Println(m["Webster"])
}

func showMap3() {
	m := make(map[string]*Red)
	m["Webster"] = new(Red) //{5,5,"Paul Webster"}
	m["Webster"].name = "Paul Webster"
	fmt.Println(m)
}

func mutateMap() {
	m := make(map[string]int)

	m["Answer"] = 42
	fmt.Println("The value:", m["Answer"])

	m["Answer"] = 48
	fmt.Println("The value:", m["Answer"])

	delete(m, "Answer")
	fmt.Println("The value:", m["Answer"])

	v, ok := m["Answer"]
	fmt.Println("The value:", v, "Present?", ok)
}

func WordCount(s string) map[string]int {
	fields := strings.Fields(s)
	rc := make(map[string]int)
	for _,v := range fields {
		rc[v]++
	}
	return rc
}

func testWordCount() {
	wc.Test(WordCount)
}

func showFunc() {
	fn := func(x,y int)int {
		return x+y
	}
	fmt.Println(fn(8,9))
	
	fmt.Println(compute(fn))
}

func compute(fn func(x,y int) int) int {
	return fn(8,5)
}

func adder() func(int) int {
	sum := 0
	// each func is bound to its own `sum` variable
	return func(x int) int {
		sum += x
		return sum
	}
}

func showAdder() {
	pos, neg := adder(), adder()
	for i := 0; i < 10; i++ {
		fmt.Println(
			pos(i),
			neg(-2*i),
		)
	}
}

func fibonacci() func() int {
	last := 0
	cur := 1
	return func() int {
		rc := last + cur
		last = cur
		cur = rc
		return rc
	}
}


func showFibonacci() {
	f := fibonacci()
	for i := 0; i < 10; i++ {
		fmt.Println(f())
	}
}

type MyFloat float64

func (f MyFloat) Abs() float64 {
	if f < 0 {
		return float64(-f)
	}
	return float64(f)
}


type Vertex struct {
	X, Y float64
}

func (v *Vertex) Abs() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}


func showStructMethod() {
	v := new(Vertex)
	v.X = 3
	v.Y = 4
	fmt.Println(v.Abs())
}

// a variable of the interface type can hold any
// value that implements those methods
type AbsType interface {
	Abs() float64
}

func showInter() {
	var a AbsType
	
	f := MyFloat(-math.Sqrt2)
	v := Vertex{3, 4}

	a = f  // a MyFloat implements Abser
	a = &v // a *Vertex implements Abser


	fmt.Println(a.Abs())	
}


type Person struct {
	Name string
	Age  int
}

// for the Stringer interface
func (p *Person) String() string {
	return fmt.Sprintf("%v (%v years)", p.Name, p.Age)
}

func showPerson() {
	a := new(Person)
	a.Name = "Arthur Dent"
	a.Age = 42
	z := new(Person)
	z.Name = "Zaphod Beeblebrox"
	z.Age = 9001
	fmt.Println(a, z)

}

type IPAddr [4]byte

// for the Stringer interface
func (ip *IPAddr) String() string {
	return fmt.Sprintf("%v.%v.%v.%v", ip[0], ip[1], ip[2], ip[3])
}

func showIp() {
	addrs := map[string]IPAddr{
		"loopback":  {127, 0, 0, 1},
		"googleDNS": {8, 8, 8, 8},
	}
	for n, a := range addrs {
		fmt.Printf("%v: %v\n", n, &a)
	}
}

func showReader() {
	r := strings.NewReader("Hello, Reader!")

	b := make([]byte, 8)
	for {
		n, err := r.Read(b)
		fmt.Printf("n = %v err = %v b = %v\n", n, err, b)
		fmt.Printf("b[:n] = %q\n", b[:n])
		if err == io.EOF {
			break
		}
	}

}

type MyReader struct{}

func (MyReader) Read(b []byte) (int, error) {
	for i := range b {
		b[i] = 'A'
	}
	return len(b), nil
}

func showReader2() {
	reader.Validate(MyReader{})
}

type rot13Reader struct {
	r io.Reader
}

func (r13 *rot13Reader) Read(b []byte) (int, error) {
	i,err := r13.r.Read(b)
	for j := 0; j<i; j++ {
		if b[j] >= 'a' && b[j] <= 'm' {
			b[j] += 13
		} else if b[j] >= 'n' && b[j] <= 'z' {
			b[j] -= 13
		} else if b[j] >= 'A' && b[j] <= 'M' {
			b[j] += 13
		} else if b[j] >= 'N' && b[j] <= 'Z' {
			b[j] -= 13
		}
	}
	return i,err
}

func showReader3() {
	s := strings.NewReader("Lbh penpxrq gur pbqr!")
	r := rot13Reader{s}
	io.Copy(os.Stdout, &r)
}


func main() {
	showReader3()
}

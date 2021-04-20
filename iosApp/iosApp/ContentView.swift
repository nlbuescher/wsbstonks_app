import shared
import SwiftUI

func test() -> String {
	IosKt.test { (string, error) in
		print(string ?? "oops")
	}
	return "Hello"
}

struct ContentView: View {
	var body: some View {
		Text(test())
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

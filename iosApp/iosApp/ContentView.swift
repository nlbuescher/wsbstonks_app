import shared
import SwiftUI

class ContentViewModel: ObservableObject {
	private let api = Api()
	private var timer: Timer? = nil

	@Published var isLoaded: Bool = false
	@Published var timeString: String = "NaN"
	@Published var stonks: [Stonk] = []

	func start() {
		print("start")
		if timer == nil {
			timer = Timer(timeInterval: 10, repeats: true) { timer in
				self.api.allStonks { result, error in
					guard let result = result else {
						if let error = error {
							print(error)
						}
						return
					}
					self.timeString = ApiKt.getRelativeTime(fromTimestamp: result.timestamp)
					self.stonks = result.stonks
					self.isLoaded = true
				}
			}
			timer?.fire()
		}
	}

	func stop() {
		print("stop")
		timer?.invalidate()
		timer = nil
	}
}

struct ContentView: View {
	@StateObject var viewModel = ContentViewModel()

	var body: some View {
		VStack {
			if viewModel.isLoaded {
				Text(viewModel.timeString)
				Text(viewModel.stonks.description)
			}
		}
			.onAppear {
				viewModel.start()
			}
			.onReceive(NotificationCenter.default.publisher(for: UIApplication.willEnterForegroundNotification)) { _ in
				viewModel.start()
			}
			.onReceive(NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification)) { _ in
				viewModel.stop()
			}
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

/**
 * SECTION 4 – MEDITATION
 * Slide 0: Meditation main screen
 * Slide 1: Open Meditation Session screen
 */
import BottomNav from './BottomNav.jsx'

export default function Meditation({ currentSlide, onNext, onPrevious }) {
  if (currentSlide === 0) {
    return (
      <div className="bg-bg-splash min-h-screen flex flex-col">
        <div className="flex-1 px-6 pt-28">
          <button type="button" className="text-desc-gray text-[16px] font-medium mx-auto block" onClick={onPrevious}>
            ‹ Back
          </button>
          <p className="text-desc-gray text-[14px] text-center mt-10">
            Tap below to start a guided session
          </p>
          <button
            type="button"
            onClick={onNext}
            className="w-full h-[62px] rounded-2xl bg-gradient-to-r from-[#B39DDB] to-[#7E57C2] text-white text-[18px] font-bold mt-4 shadow-[0_14px_28px_rgba(126,87,194,0.30)] flex items-center justify-center gap-3"
          >
            <span className="text-[20px]">🧘</span>
            <span>Open Meditation Session</span>
          </button>
        </div>

        <BottomNav />
      </div>
    )
  }

  if (currentSlide === 1) {
    return (
      <div className="bg-bg-splash min-h-screen px-6 pt-6">
        <div className="flex items-center justify-between">
          <button type="button" onClick={onPrevious} className="w-10 h-10 rounded-2xl bg-white/70 border border-stroke-gray/60 flex items-center justify-center text-desc-gray">
            ‹
          </button>
          <div className="text-title-blue text-[18px] font-bold">Meditation</div>
          <button type="button" className="w-10 h-10 rounded-2xl bg-white/70 border border-stroke-gray/60 flex items-center justify-center text-desc-gray">
            ⚙
          </button>
        </div>

        <div className="flex justify-center gap-3 mt-6">
          <div className="px-6 py-3 rounded-2xl bg-[#B3E5FC]/70 text-title-blue flex items-center gap-2 shadow-[0_10px_18px_rgba(26,35,126,0.06)]">
            <span>🌊</span>
            <span className="font-bold">Calm</span>
          </div>
          <div className="px-6 py-3 rounded-2xl bg-white/60 text-desc-gray flex items-center gap-2 border border-stroke-gray/40">
            <span>🎯</span>
            <span className="font-semibold">Focus</span>
          </div>
          <div className="px-6 py-3 rounded-2xl bg-white/60 text-desc-gray flex items-center gap-2 border border-stroke-gray/40">
            <span>🌙</span>
            <span className="font-semibold">Sleep</span>
          </div>
        </div>

        <div className="flex justify-center mt-12">
          <div className="relative w-[280px] h-[280px] flex items-center justify-center">
            <div className="absolute inset-0 rounded-full border-[16px] border-stroke-gray/25" />
            <div className="absolute inset-[28px] rounded-full bg-white/70" />
            <div className="relative z-10 text-center">
              <div className="text-title-blue text-[54px] font-extrabold tracking-tight">05:00</div>
              <div className="text-desc-gray text-[16px] mt-1">Ready to start</div>
            </div>
          </div>
        </div>

        <div className="flex justify-center gap-4 mt-8">
          <div className="px-5 py-2 rounded-2xl bg-button-blue text-white font-bold shadow-[0_10px_18px_rgba(74,144,226,0.25)]">
            5m
          </div>
          <div className="px-5 py-2 rounded-2xl bg-white/60 text-desc-gray border border-stroke-gray/40 font-semibold">10m</div>
          <div className="px-5 py-2 rounded-2xl bg-white/60 text-desc-gray border border-stroke-gray/40 font-semibold">15m</div>
          <div className="px-5 py-2 rounded-2xl bg-white/60 text-desc-gray border border-stroke-gray/40 font-semibold">20m</div>
        </div>

        <div className="flex justify-center items-center gap-8 mt-12">
          <button type="button" className="w-14 h-14 rounded-2xl bg-white/70 border border-stroke-gray/60 flex items-center justify-center text-desc-gray text-[22px]">
            ↺
          </button>
          <button type="button" className="w-[78px] h-[78px] rounded-full bg-button-blue text-white flex items-center justify-center shadow-[0_14px_28px_rgba(74,144,226,0.35)]">
            <span className="text-[24px] ml-1">▶</span>
          </button>
          <button type="button" className="w-14 h-14 rounded-2xl bg-white/70 border border-stroke-gray/60 flex items-center justify-center text-desc-gray text-[22px]">
            ♪
          </button>
        </div>
      </div>
    )
  }

  return null
}
